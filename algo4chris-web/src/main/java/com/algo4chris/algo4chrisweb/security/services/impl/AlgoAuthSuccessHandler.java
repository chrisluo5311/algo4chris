package com.algo4chris.algo4chrisweb.security.services.impl;

import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.utils.WebUtils;
import com.algo4chris.algo4chrisweb.payload.request.AlgoOAuth2User;
import com.algo4chris.algo4chrisweb.payload.response.JwtResponse;
import com.algo4chris.algo4chrisweb.security.services.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class AlgoAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    LoginService loginService;

    @Resource
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String ip = WebUtils.getIp(request);
        AlgoOAuth2User oauthUser = (AlgoOAuth2User) authentication.getPrincipal();
        try{
            JwtResponse jwtResponse = loginService.processOAuthPostLogin(oauthUser, ip);
            log.info("【ip】:{} Google登入成功",ip);
            //response body
            objectMapper.writeValue(response.getOutputStream(), MgrResponseDto.success(jwtResponse));
        } catch (Exception e){
            objectMapper.writeValue(response.getOutputStream(), MgrResponseDto.error(MgrResponseCode.GOOGLE_LOGIN_FAIL));
        }
    }
}
