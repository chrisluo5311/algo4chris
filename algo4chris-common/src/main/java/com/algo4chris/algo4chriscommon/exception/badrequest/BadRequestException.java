package com.algo4chris.algo4chriscommon.exception.badrequest;

import com.algo4chris.algo4chriscommon.exception.base.BaseException;
import com.algo4chris.algo4chriscommon.exception.modules.Modules;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;

public class BadRequestException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(MgrResponseCode code, Object[] args) {
        super(Modules.BAD_REQUEST, code, args, code.getMessage());
    }

    public BadRequestException(String code,Object[] args,String messages){
        super(Modules.BAD_REQUEST,code,args,messages);
    }

    public BadRequestException(int code,String message) {
        super(Modules.BAD_REQUEST, String.valueOf(code), null, message);
    }

}
