package com.algo4chris.algo4chriscommon.exceptionhandler;

import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chriscommon.exceptionhandler.enums.ExceptionResponseEnum;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@ControllerAdvice
@Order()
public class DefaultExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MgrResponseDto errorHandler(Exception e) {
        e.printStackTrace();
        return ExceptionResponseEnum.getMgrResponseFromException(e);
    }

}
