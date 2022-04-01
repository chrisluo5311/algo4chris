package com.algo4chris.algo4chrisweb.security.services;


import com.algo4chris.algo4chrisdal.models.User;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chrisweb.payLoad.request.LoginRequest;
import com.algo4chris.algo4chrisweb.payLoad.request.SignupRequest;
import com.algo4chris.algo4chrisweb.payLoad.request.TokenRefreshRequest;
import com.algo4chris.algo4chrisweb.payLoad.response.JwtResponse;
import com.algo4chris.algo4chrisweb.payLoad.response.TokenRefreshResponse;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    JwtResponse loginMember(LoginRequest loginRequest);

    User signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest);

    TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest);

    void logOutUser(SessionEntity sessionEntity, HttpServletRequest servletRequest);
}
