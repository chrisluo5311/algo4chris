package com.algo4chris.algo4chrisweb.security.services.impl;

import com.algo4chris.algo4chriscommon.common.constant.InnerRouteConst;
import com.algo4chris.algo4chriscommon.utils.LogUtil;
import com.algo4chris.algo4chriscommon.utils.RandomUtil;
import com.algo4chris.algo4chriscommon.utils.WebUtils;
import com.algo4chris.algo4chrisweb.payload.request.AlgoOAuth2User;
import com.algo4chris.algo4chrisweb.payload.response.JwtResponse;
import com.algo4chris.algo4chrisweb.security.services.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.algo4chris.algo4chriscommon.common.constant.JwtConstants.AUTHORIZATION_CODE_KEY;
import static com.algo4chris.algo4chriscommon.common.constant.JwtConstants.BEARER_CODE_KEY;

@Slf4j
public class AlgoAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value(value = "${server.port}")
    private String port;

    @Resource
    LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        LogUtil.setMDC(LogUtil.MDCKey.RandomCode, RandomUtil.getRandom(10));
        String ip = WebUtils.getIp(request);
        log.info("【ip】:{} Google登入",ip);
        AlgoOAuth2User oauthUser = (AlgoOAuth2User) authentication.getPrincipal();
        try{
            JwtResponse jwtResponse = loginService.processOAuthPostLogin(oauthUser, ip);
            String redirectionUrl = UriComponentsBuilder.fromUriString(InnerRouteConst.http + port + InnerRouteConst.homeUrl)
                    .queryParam("memberName", jwtResponse.getMemberName())
                    .queryParam(AUTHORIZATION_CODE_KEY,BEARER_CODE_KEY+jwtResponse.getToken())
                    .queryParam("refreshToken",jwtResponse.getRefreshToken())
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
        } catch (Exception e){
            response.sendRedirect(InnerRouteConst.http + port + "/403");
        } finally {
            MDC.clear();
        }
    }
}
