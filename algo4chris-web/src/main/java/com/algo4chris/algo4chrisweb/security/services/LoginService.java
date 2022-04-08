package com.algo4chris.algo4chrisweb.security.services;


import com.algo4chris.algo4chrisdal.models.User;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chrisweb.payLoad.request.LoginRequest;
import com.algo4chris.algo4chrisweb.payLoad.request.SignupRequest;
import com.algo4chris.algo4chrisweb.payLoad.request.TokenRefreshRequest;
import com.algo4chris.algo4chrisweb.payLoad.response.JwtResponse;
import com.algo4chris.algo4chrisweb.payLoad.response.TokenRefreshResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 登入/登出/註冊service
 *
 * @author chris
 * @Date 2022/02/29
 */
public interface LoginService {

    /**
     * 登入<br>
     * jwt token 时效为1小时<br>
     * refresh token 时效为24小时
     *
     * @param loginRequest 登入請求
     * @return JwtResponse
     * */
    JwtResponse loginMember(LoginRequest loginRequest);

    /**
     * 注册<br>
     * 未輸入權限一律預設為一般使用者
     *
     * @param signUpRequest 注册请求
     * @param servletRequest HttpServletRequest
     * @return User 用户
     * */
    User signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest);

    /**
     * 獲得新token
     *
     * @param refreshRequest Token Refresh 請求
     * @return TokenRefreshResponse
     * */
    TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest);

    /**
     * 登出用戶
     *
     * @param servletRequest HttpServletRequest
     * @param sessionEntity Session
     * */
    void logOutUser(SessionEntity sessionEntity, HttpServletRequest servletRequest);
}
