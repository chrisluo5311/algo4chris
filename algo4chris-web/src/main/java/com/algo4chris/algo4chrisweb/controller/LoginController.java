package com.algo4chris.algo4chrisweb.controller;

import com.algo4chris.algo4chriscommon.common.constant.JwtConstants;
import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chriscommon.utils.RequestHolder;
import com.algo4chris.algo4chriscommon.utils.WebUtils;
import com.algo4chris.algo4chrislogging.annotation.Log;
import com.algo4chris.algo4chrisweb.payload.request.LoginRequest;
import com.algo4chris.algo4chrisweb.payload.request.SignupRequest;
import com.algo4chris.algo4chrisweb.payload.request.TokenRefreshRequest;
import com.algo4chris.algo4chrisweb.payload.response.JwtResponse;
import com.algo4chris.algo4chrisweb.payload.response.TokenRefreshResponse;
import com.algo4chris.algo4chrisdal.models.Member;
import com.algo4chris.algo4chrisweb.security.services.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登入/登出/註冊類
 *
 * @author chris
 * */
@Api(tags = "登入登出註冊")
@Slf4j
@RestController
@RequestMapping("/api")
public class LoginController {

    @Resource
    LoginService loginService;

    @ApiOperation(value = "用户登入", httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public MgrResponseDto<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("用戶:{} 發送登入請求", loginRequest.getMemberName());
        JwtResponse jwtResponse = loginService.loginMember(loginRequest,RequestHolder.getHttpServletRequest());
        //在header中設置jwtToken
        RequestHolder.getHttpServletResponse()
                .setHeader(JwtConstants.AUTHORIZATION_CODE_KEY,
                        JwtConstants.BEARER_CODE_KEY + jwtResponse.getToken());
        return MgrResponseDto.success(jwtResponse);
    }

    @Log(value = "用戶註冊")
    @ApiOperation(value = "用户註冊", httpMethod = "POST")
    @RequestMapping(value = "/signUpAccount",method = RequestMethod.POST)
    public MgrResponseDto<JwtResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info("新用戶:{} 註冊帳號", signUpRequest.getMemberName());
        JwtResponse jwtResponse = loginService.signUp(signUpRequest,RequestHolder.getHttpServletRequest());
        //重導向至首頁登入

        return MgrResponseDto.success(jwtResponse);
    }

    @ApiOperation(value = "獲得新的Token",httpMethod = "POST")
    @RequestMapping(value = "/refreshToken",method = RequestMethod.POST)
    public MgrResponseDto<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse tokenRefreshResponse = loginService.refreshToken(request);
        //在header中設置jwtToken
        RequestHolder.getHttpServletResponse()
                .setHeader(JwtConstants.AUTHORIZATION_CODE_KEY,
                        JwtConstants.BEARER_CODE_KEY + tokenRefreshResponse.getAccessToken());
        return MgrResponseDto.success(tokenRefreshResponse);
    }
}
