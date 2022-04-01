package com.algo4chris.algo4chrisweb.controller.logout;

import com.algo4chris.algo4chrisweb.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    @Resource
    JwtUtils jwtUtils;


    @Override
    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        String jwt = jwtUtils.parseJwt(httpServletRequest);
        if( jwt!=null) {
            String userName = jwtUtils.getUserNameIgnoredExpired(jwt);
            log.info("【LogoutHandler】:用戶名: {} 點擊登出",userName);
        }
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        }
    }
}
