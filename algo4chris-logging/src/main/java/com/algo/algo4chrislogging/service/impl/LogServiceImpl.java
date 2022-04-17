package com.algo.algo4chrislogging.service.impl;

import cn.hutool.json.JSONUtil;
import com.algo.algo4chrislogging.annotation.Log;
import com.algo4chris.algo4chrisdal.models.OperateLog;
import com.algo4chris.algo4chrisdal.repository.OperateLogRepository;
import com.algo.algo4chrislogging.service.LogService;
import com.algo4chris.algo4chriscommon.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

    @Resource
    OperateLogRepository logRepository;

    @Override
    public void save(String memberName, String browser, String ip, ProceedingJoinPoint joinPoint,OperateLog operateLog) {
        if (operateLog == null) {
            throw new IllegalArgumentException("OperateLog不可為null!");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 方法路徑
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        // 描述
        operateLog.setDescription(logAnnotation.value());

        operateLog.setRequestIp(ip);
        operateLog.setAddress(WebUtils.getCityInfo(operateLog.getRequestIp()));
        operateLog.setMethod(methodName);
        operateLog.setMembername(memberName);
        operateLog.setParams(getParameter(method, joinPoint.getArgs()));
        operateLog.setBrowser(browser);
        logRepository.save(operateLog);
    }

    /**
     * 根據方法和傳入的參數獲取請求參數
     * @param method 方法
     * @param args 傳入的參數
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //解析RequestBody的參數
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>(4);
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.isEmpty()) {
            return "";
        }
        return argList.size() == 1 ? JSONUtil.toJsonStr(argList.get(0)) : JSONUtil.toJsonStr(argList);
    }

}
