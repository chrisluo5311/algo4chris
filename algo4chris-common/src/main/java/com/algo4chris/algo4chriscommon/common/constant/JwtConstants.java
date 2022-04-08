package com.algo4chris.algo4chriscommon.common.constant;

/**
 * jwt 通用常數
 *
 * @author chris
 * @Date 2022/01/29
 * */
public class JwtConstants {

    /**
     *  取 Jwt token 的 code key
     *
     * */
    public static final String AUTHORIZATION_CODE_KEY = "Authorization";

    /**
     *  取 Jwt token 的前綴
     *
     * */
    public static final String BEARER_CODE_KEY = "Bearer ";



    /** 登出redis black list key 時效 */
    public static final int LOGOUT_EXPIRATION_TIME = 1;

}
