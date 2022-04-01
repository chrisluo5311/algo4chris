package com.algo4chris.algo4chriscommon.exception.tokenrefresh;


import com.algo4chris.algo4chriscommon.exception.base.BaseException;
import com.algo4chris.algo4chriscommon.exception.modules.Modules;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;

/**
 * 處理token refresh接口異常類
 *
 * @author chris
 * */
public class TokenRefreshException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TokenRefreshException(MgrResponseCode mgrResponseCode, String token) {
        super(Modules.REFRESH_TOKEN,mgrResponseCode,new Object[]{token},mgrResponseCode.getMessage());
    }
}
