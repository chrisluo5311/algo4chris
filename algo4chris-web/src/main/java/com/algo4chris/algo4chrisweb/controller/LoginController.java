package com.algo4chris.algo4chrisweb.controller;

import com.algo4chris.algo4chriscommon.common.constant.JwtConstants;
import com.algo4chris.algo4chriscommon.common.constant.RoleConstants;
import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chrisdal.models.User;
import com.algo4chris.algo4chrisweb.payLoad.request.LoginRequest;
import com.algo4chris.algo4chrisweb.payLoad.request.SignupRequest;
import com.algo4chris.algo4chrisweb.payLoad.request.TokenRefreshRequest;
import com.algo4chris.algo4chrisweb.payLoad.response.JwtResponse;
import com.algo4chris.algo4chrisweb.payLoad.response.TokenRefreshResponse;
import com.algo4chris.algo4chrisweb.security.services.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    public MgrResponseDto<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                        HttpServletRequest servletRequest,
                                                        HttpServletResponse servletResponse) {
        JwtResponse jwtResponse = loginService.loginMember(loginRequest);
        //在header中設置jwtToken
        servletResponse.setHeader(JwtConstants.AUTHORIZATION_CODE_KEY,JwtConstants.BEARER_CODE_KEY + jwtResponse.getToken());
        return MgrResponseDto.success(jwtResponse);
    }

    @ApiOperation(value = "用户註冊", httpMethod = "POST")
    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public MgrResponseDto<User> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                             HttpServletRequest servletRequest) {
        User user = loginService.signUp(signUpRequest,servletRequest);
        return MgrResponseDto.success(user);
    }

    @ApiOperation(value = "獲得新的Token",httpMethod = "POST")
    @RequestMapping(value = "/refreshToken",method = RequestMethod.POST)
    public MgrResponseDto<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request,
                                                             HttpServletResponse servletResponse) {
        TokenRefreshResponse tokenRefreshResponse = loginService.refreshToken(request);
        //在header中設置jwtToken
        servletResponse.setHeader(JwtConstants.AUTHORIZATION_CODE_KEY, JwtConstants.BEARER_CODE_KEY + tokenRefreshResponse.getAccessToken());
        return MgrResponseDto.success(tokenRefreshResponse);
    }
}
