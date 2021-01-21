package com.zzh.currentlimiting.controller;

import com.zzh.currentlimiting.aspects.limit.Limit;
import com.zzh.currentlimiting.aspects.log.SysLog;
import com.zzh.currentlimiting.bean.Person;
import com.zzh.currentlimiting.bean.R;
import com.zzh.currentlimiting.service.impl.pointCutImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 张子行
 * @class 测试限流，日志Controller
 */
@RestController
@RequestMapping
public class logController {
    @Autowired
    com.zzh.currentlimiting.service.impl.pointCutImpl pointCutImpl;

    @PostMapping("/login")
    @SysLog(description = "用户登录接口")
    @Limit(maxLimit = 10, cycle = 1)
    public R login(Person person, HttpServletRequest servletRequest) {
        pointCutImpl.say();
        return R.ok();
    }

    @PostMapping("/login2")
    @SysLog(description = "用户登录接口2")
    @Limit(maxLimit = 5, cycle = 1)
    public R login2(Person person, HttpServletRequest servletRequest) {
        pointCutImpl.say();
        return R.ok();
    }
}
