package com.zzh.currentlimiting.config;

import com.zzh.currentlimiting.interceptor.currentLimiting2;
import com.zzh.currentlimiting.interceptor.currentLimitingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 张子行
 * @class
 */
@Configuration
public class myMvcConfig implements WebMvcConfigurer {
    @Bean
    public currentLimitingInterceptor getCurrentLimitingInterceptor() {
        return new currentLimitingInterceptor();
    }

    @Bean
    public currentLimiting2 getCurrentLimiting2Interceptor() {
        return new currentLimiting2();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(getCurrentLimitingInterceptor());
    }
}
