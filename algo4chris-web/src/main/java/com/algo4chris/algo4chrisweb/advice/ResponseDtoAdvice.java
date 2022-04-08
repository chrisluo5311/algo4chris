package com.algo4chris.algo4chrisweb.advice;

import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chrisweb.advice.logging.LoggingService;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 印製ResponseBody的HttpServletResponse響應
 *
 * @author chris
 * */
@ControllerAdvice
public class ResponseDtoAdvice implements ResponseBodyAdvice<MgrResponseDto> {

    @Resource
    LoggingService loggingService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType().isAssignableFrom(MgrResponseDto.class);
    }

    @SneakyThrows
    @Override
    public MgrResponseDto beforeBodyWrite(MgrResponseDto responseDto,
                                          MethodParameter returnType,
                                          MediaType mediaType,
                                          Class<? extends HttpMessageConverter<?>> aClass,
                                          ServerHttpRequest serverHttpRequest,
                                          ServerHttpResponse serverHttpResponse) {
        if (serverHttpRequest instanceof ServletServerHttpRequest && serverHttpResponse instanceof ServletServerHttpResponse) {
            loggingService.logResponse(((ServletServerHttpRequest) serverHttpRequest).getServletRequest(),
                    ((ServletServerHttpResponse) serverHttpResponse).getServletResponse(), responseDto);
        }
        return responseDto;
    }
}
