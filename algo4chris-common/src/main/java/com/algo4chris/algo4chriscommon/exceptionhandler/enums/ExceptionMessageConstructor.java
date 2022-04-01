package com.algo4chris.algo4chriscommon.exceptionhandler.enums;


import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;

/**
 * 建構 response 的 interface
 * */
public interface ExceptionMessageConstructor {

    MgrResponseDto getMgrResponse(Exception e);
}
