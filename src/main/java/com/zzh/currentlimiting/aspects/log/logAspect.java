package com.zzh.currentlimiting.aspects.log;

import com.zzh.currentlimiting.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * @author 张子行
 * @class
 */
@Aspect
@Component
@Slf4j
public class logAspect {
    @Pointcut("@annotation(com.zzh.currentlimiting.aspects.log.SysLog)")
    public void controllerLog() {
    }

    @Before("controllerLog()")
    public void before(JoinPoint joinPoint) {
        /**
         * TODO 这里可以收集用户的一些ip，name等信息，然后插入数据库
         */
        saveLog(joinPoint);
    }

    /**
     * @param
     * @method 异步保存日志信息
     */
    @Async
    Boolean saveLog(JoinPoint joinPoint) {
        //获取对应的controller名字
        String controllerName = joinPoint.getTarget().getClass().getName();
        //从servlet中获取一些信息
        ServletRequestAttributes servlet = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String username = servlet.getRequest().getParameter("username");
        String description = null;
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        Method currentMethod = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        try {
            //获取当前作用的方法
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        description = currentMethod.getAnnotation(SysLog.class).description();
//        //获取切面作用的目标类的class
//        Class<?> aClass = joinPoint.getTarget().getClass();
//        //获取作用方法的名字
//        String methodName = joinPoint.getSignature().getName();
//        //根据class获取所有该class下的方法
//        Method[] methods = aClass.getMethods();
//        //获取作用方法上的参数
//        Object[] args = joinPoint.getArgs();
//        //遍历所有的方法
//        for (Method method : methods) {
//            //根据获取到的方法名字，匹配获取该方法
//            if (methodName.equals(method.getName())) {
//                Class<?>[] parameterTypes = method.getParameterTypes();
//                //方法中的参数匹配，精确匹配方法
//                if (parameterTypes.length == args.length) {
//                    description = method.getAnnotation(SysLog.class).description();
//                }
//            }
//        }
        String ipAddr = IPUtils.getIpAddr(servlet.getRequest());
        log.info("日志收集：" + ipAddr + "请求了" + controllerName + "下面的" + description);
        return true;
    }


}
