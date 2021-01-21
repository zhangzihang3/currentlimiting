package com.zzh.currentlimiting.aspects.limit;

import com.zzh.currentlimiting.bean.Person;
import com.zzh.currentlimiting.bean.R;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author 张子行
 * @class 限流切面
 */
@Aspect
@Component
@Slf4j
public class limitAspect {
    private ConcurrentHashMap<String, Semaphore> semaphores = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.zzh.currentlimiting.aspects.limit.Limit)")
    public void limit() {

    }

    /**
     * @param
     * @method 对接口进行限流
     */
    @Around("limit()")
    public R before(ProceedingJoinPoint joinPoint) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //获取被该注解作用的对象
        Object target = joinPoint.getTarget();

        //获取被该注解作用的对象名字
        String targetName = target.getClass().getName();
        //获取被该注解作用的对象的class
        Class<?> targetClass = target.getClass();
        //获取请求的参数
        Object[] methodParam = joinPoint.getArgs();
        //获取被该注解作用的方法的名字
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        StringBuffer bufferKey = new StringBuffer().append(methodName).append(targetName);
        String key = String.valueOf(bufferKey);
        Method[] methods = targetClass.getMethods();
        Limit annotation = null;
        //遍历所有的方法
        for (Method method : methods) {
            //根据获取到的方法名字，匹配获取该方法
            if (methodName.equals(method.getName())) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                //方法中的参数匹配，精确匹配方法
                if (parameterTypes.length == args.length) {
                    annotation = method.getAnnotation(Limit.class);
                }
            }
        }
        if (null != annotation) {
            Semaphore semaphore = semaphores.get(key);
            if (null == semaphore) {
                //semaphores.put()
                //初始化各个接口的访问流量
                System.out.println("maxLimit:" + annotation.maxLimit());
                semaphores.putIfAbsent(String.valueOf(key), new Semaphore(annotation.maxLimit()));
                semaphore = semaphores.get(key);
            }
            try {
                //当达到最大的访问的流量后，只有等有空闲的流量时，别的人才能加入
                if (semaphore.tryAcquire()) {
                    //执行方法
                    joinPoint.proceed();
                    log.info("成功");
                    return R.ok();
                }
                log.error(methodName + "限流");
                return R.error();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
        log.info(methodName + "成功");
        return R.ok();
    }

}