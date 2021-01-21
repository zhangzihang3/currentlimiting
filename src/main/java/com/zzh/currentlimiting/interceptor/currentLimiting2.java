package com.zzh.currentlimiting.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author 张子行
 * @class
 */
@Slf4j
public class currentLimiting2 implements HandlerInterceptor {
    //每一秒生成俩个令牌
    private RateLimiter rateLimiter = RateLimiter.create(2, 1, TimeUnit.SECONDS);

    /**
     * @param
     * @method return true放行 return false拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (rateLimiter.tryAcquire()) {
            //获取到令牌可以直接放行
            log.info("放行");
            return true;
        }
        //TODO 可以执行自己的拦截逻辑
        log.warn("拦截");
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        //执行完方法体才会执行这里的逻辑
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
