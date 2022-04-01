package com.algo4chris.algo4chriscommon.exception.user;

import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;

/**
 * 用戶jwt異常類
 *
 * @author chris
 * */
public class UserJwtException extends Exception{

    private MgrResponseCode mgrResponseCode;

    private String code;

    private Object[] args;

    public UserJwtException(String code,Object[] args,String messages){
        super(messages);
        this.code = code;
        this.args = args;
    }

    public UserJwtException(MgrResponseCode code, Object[] args) {
        super(code.getMessage());
        this.mgrResponseCode = code;
        this.args = args;
    }

    public String getCode(){
        return this.code;
    }

    public MgrResponseCode getMgrResponseCode(){
        return this.mgrResponseCode;
    }

    public Object[] getArgs() {
        return args;
    }
}
