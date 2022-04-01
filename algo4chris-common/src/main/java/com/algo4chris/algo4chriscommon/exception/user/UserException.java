package com.algo4chris.algo4chriscommon.exception.user;


import com.algo4chris.algo4chriscommon.exception.base.BaseException;
import com.algo4chris.algo4chriscommon.exception.modules.Modules;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;

/**
 * 用户信息異常類
 *
 * @author chris
 */
public class UserException extends BaseException {

    private static final long serialVersionUID = 1L;

    public UserException(MgrResponseCode code, Object[] args) {
        super(Modules.USER, code, args, code.getMessage());
    }

    public UserException(String code,Object[] args,String messages){
        super(Modules.USER,code,args,messages);
    }
}
