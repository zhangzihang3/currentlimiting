package com.zzh.currentlimiting.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zzh.currentlimiting.aspects.limit.Limit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author 张子行
 * @class 限流拦截器
 */
@Slf4j
public class currentLimitingInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param
     * @method return true放行 return false拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String className = handlerMethod.getBean().getClass().getName();
            //没有Limit注解则执行下面的拦截逻辑
            if (!method.isAnnotationPresent(Limit.class)) {
                log.info("没有加limit注解放行");
                return true;
            }
            Limit annotation = handlerMethod.getMethod().getAnnotation(Limit.class);
            //没有加Limit注解的方法就放行
            if (annotation == null) {
                log.info("limit为null放行");
                return true;
            }
            Integer maxLimit = annotation.maxLimit();
            Integer cycle = annotation.cycle();
            Integer rMaxLimit = null;
            //TODO 实际生产环境换成真实ip
            String key = "127.0.0.1"+className+method.getName();
            String value = redisTemplate.opsForValue().get(key);
            if (StrUtil.isNotEmpty(value)) {
                rMaxLimit = Integer.valueOf(value);
            }
            //第一此访问此接口，设置初始访问次数为1
            if (rMaxLimit == null) {
                redisTemplate.opsForValue().set(key, "1", cycle, TimeUnit.SECONDS);
            }
            //如果访问次数没有达到Limit注解上标注的最大访问次数，设置访问次数++
            else if (rMaxLimit <= maxLimit) {
                redisTemplate.opsForValue().set(key, rMaxLimit + 1 + "", cycle, TimeUnit.SECONDS);
            }
            //其他的情况，表明需要做限流了，返回一些提示信息
            else {
                ServletOutputStream outputStream = null;
                try {
                    response.setHeader("Content-type", "application/json; charset=utf-8");
                    log.warn("请稍后尝试");
                    response.getWriter().append("");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }
        }
        log.info("放行");
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
