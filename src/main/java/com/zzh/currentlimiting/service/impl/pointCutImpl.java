package com.zzh.currentlimiting.service.impl;

import com.zzh.currentlimiting.aspects.log.SysLog;
import com.zzh.currentlimiting.service.pointCut;
import org.springframework.stereotype.Service;

@Service
public class pointCutImpl implements pointCut {
    @Override
    @SysLog(description = "say方法")
    public Boolean say() {
        System.out.println("执行say方法");
        return true;
    }


}
