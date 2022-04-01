package com.algo4chris.algo4chriscommon.exception.base;

import com.algo4chris.algo4chriscommon.exception.modules.Modules;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;

/**
 *  基礎異常
 *
 *  @author chris
 * */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 所屬模塊
     */
    private Modules module;

    /**
     * 錯誤碼
     */
    private String code;

    /**
     * 錯誤碼對應參數
     */
    private Object[] args;

    /**
     * 錯誤消息
     */
    private String message;

    public BaseException(Modules module, String code, Object[] args, String message) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.message = message;
    }

    public BaseException(Modules module, MgrResponseCode code, Object[] args, String message) {
        this.module = module;
        this.code = code.getCode();
        this.args = args;
        this.message = message;
    }

    public BaseException(Modules module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public Modules getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getMessage() {
        return message;
    }

}
