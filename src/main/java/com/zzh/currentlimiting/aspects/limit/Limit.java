package com.zzh.currentlimiting.aspects.limit;

import java.lang.annotation.*;

/**
 * @author 张子行
 * @class 限流注解，秒级访问多少次
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value = ElementType.METHOD)
public @interface Limit {
    //访问次数
    int maxLimit() default 10;
    //周期
    int cycle() default 1;
}
