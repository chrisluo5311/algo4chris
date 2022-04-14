package com.algo4chris.algo4chrisweb.security.services.impl;

import com.algo4chris.algo4chriscommon.utils.IpUtils;
import com.algo4chris.algo4chrisweb.payload.request.AlgoOAuth2User;
import com.algo4chris.algo4chrisweb.security.services.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AlgoAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String ip = IpUtils.getIpAddr(request);
        AlgoOAuth2User oauthUser = (AlgoOAuth2User) authentication.getPrincipal();
        loginService.processOAuthPostLogin(oauthUser,ip);
        response.sendRedirect("/loginSuccess");
    }
}
