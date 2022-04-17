package com.algo4chris.algo4chrislogging.service;

import com.algo4chris.algo4chrisdal.models.OperateLog;
import org.aspectj.lang.ProceedingJoinPoint;

public interface LogService {

    void save(String memberName, String browser, String ip, ProceedingJoinPoint joinPoint, OperateLog operateLog);

}
