package com.algo4chris.algo4chrisweb.advice;

import com.algo4chris.algo4chrisweb.advice.annotations.HttpRequestElements;
import com.algo4chris.algo4chrisweb.advice.annotations.HttpRequestLog;
import com.algo4chris.algo4chrisweb.advice.logging.LoggingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 印製RequestBody的HttpServletRequest請求
 *
 * @author chris
 * */
@Slf4j
@ControllerAdvice
public class RequestDtoAdvice extends RequestBodyAdviceAdapter {

    @Resource
    LoggingService loggingService;

    @Resource
    HttpServletRequest httpServletRequest;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        String controllerMethodName = parameter.getMethod().getName();
        Annotation[] annotations = parameter.getMethodAnnotations();
        HttpRequestLog httpLog = null;
        httpLog = (HttpRequestLog) Arrays.stream(annotations).filter(a->a.annotationType().equals(HttpRequestLog.class)).findFirst().orElse(null);
        List<HttpRequestElements> httpElementsList = null;
        if (httpLog != null) {
            if(!httpLog.enable()){
                //代表不要寫HttpServletRequest log
                return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
            }
            //代表指定輸出那些Log
            if(!Arrays.equals(httpLog.value(), new HttpRequestElements[]{HttpRequestElements.All})){
                log.info("接收方法名:{} 指定輸出Http種類:{}",controllerMethodName,httpLog.value());
                httpElementsList = Arrays.stream(httpLog.value())
                        .sorted(Comparator.comparing(HttpRequestElements::getSequence))
                        .collect(Collectors.toList());
            }
        }
        loggingService.logRequest(httpServletRequest, body,httpElementsList);

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
