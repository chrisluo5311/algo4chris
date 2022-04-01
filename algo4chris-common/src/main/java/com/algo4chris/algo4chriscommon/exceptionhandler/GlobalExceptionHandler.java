package com.algo4chris.algo4chriscommon.exceptionhandler;

import com.algo4chris.algo4chriscommon.utils.TimeUtil;
import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chriscommon.exception.base.BaseException;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.tokenrefresh.TokenRefreshException;
import com.algo4chris.algo4chriscommon.exception.user.UserJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 處理自定義或常見 Exception 類
 *
 * @author chris
 * */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * 處理所有自定義 BaseException
     *
     * @param e BaseException
     * @return MgrResponseDto
     * */
    @ExceptionHandler(BaseException.class)
    public MgrResponseDto errorHandler(BaseException e) {
        e.printStackTrace();

        MgrResponseDto dto = new MgrResponseDto();
        dto.setCode(e.getCode());
        dto.setMessage(e.getMessage());
        dto.setData(e.getArgs());

        return dto;
    }

    /**
     * 處理jwt異常類的自定義 UserJwtException
     *
     * @param e UserJwtException
     * @return MgrResponseDto
     * */
    @ExceptionHandler(UserJwtException.class)
    public MgrResponseDto errorHandler(UserJwtException e) {
        e.printStackTrace();

        MgrResponseDto dto = new MgrResponseDto();

        if(e.getMgrResponseCode()!=null){
            dto.setCode(e.getMgrResponseCode().getCode());
        } else if(e.getCode()!=null){
            dto.setCode(e.getCode());
        }
        dto.setMessage(e.getMessage());

        return dto;
    }

    /**
     * 處理拒絕存取異常
     *
     * @param e AccessDeniedException
     * @return MgrResponseDto
     * */
    @ExceptionHandler(AccessDeniedException.class)
    public MgrResponseDto errorHandler(AccessDeniedException e) {
        e.printStackTrace();

        MgrResponseDto dto = new MgrResponseDto();
        dto.setCode(MgrResponseCode.REQUEST_ACCESS_DENIED);
        dto.setMessage(MgrResponseCode.REQUEST_ACCESS_DENIED.getMessage());
        return dto;
    }

    /**
     * 處理校驗valid異常
     *
     * @param e MethodArgumentNotValidException
     * @return MgrResponseDto
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MgrResponseDto<?> errorHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();

        MgrResponseDto<?> dto = new MgrResponseDto();

        List<FieldError> errorList = e.getBindingResult().getFieldErrors();
        //併接錯誤訊息
        String message = errorList.size() + " 項錯誤: ";
        message += errorList.stream()
                .map(error -> "參數:'" + error.getField() + "' " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        dto.setCode(MgrResponseCode.PARAM_INVALID);
        dto.setMessage(message);

        return dto;
    }

    /**
     * 處理時間轉換異常
     *
     * @param e BindException
     * @return MgrResponseDto
     * */
    @ExceptionHandler(BindException.class)
    public MgrResponseDto<?> errorHandler(BindException e) {
        e.printStackTrace();

        StringBuilder sb = new StringBuilder();
        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p ->{
                FieldError fieldError = (FieldError) p;
                Object rejectedValue = fieldError.getRejectedValue();
                if(rejectedValue instanceof Date){
                    String illegalDate = TimeUtil.customDateToString((Date) fieldError.getRejectedValue(),"YYYY-MM-DD HH:mm:ss");
                    sb.append(fieldError.getDefaultMessage()).append(" ").append(illegalDate).append(" ; ");
                } else {
                    if(rejectedValue!=null){
                        sb.append(fieldError.getDefaultMessage()).append(" ").append(rejectedValue).append("; ");
                    } else {
                        sb.append(fieldError.getDefaultMessage()).append("; ");
                    }
                }
            });
        }
        MgrResponseDto<?> dto = new MgrResponseDto();
        dto.setCode(MgrResponseCode.PARAM_INVALID);
        dto.setMessage(sb.toString());
        return dto;
    }

    /**
     * 處理token refresh接口異常
     *
     * @param e TokenRefreshException
     * @return MgrResponseDto
     * */
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public MgrResponseDto handleTokenRefreshException(TokenRefreshException e) {
        return MgrResponseDto.error(String.valueOf(HttpStatus.FORBIDDEN.value()),e.getMessage());
    }

}
