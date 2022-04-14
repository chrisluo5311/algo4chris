package com.algo4chris.algo4chrisservice.config;

import com.algo4chris.algo4chriscommon.common.constant.SessionConstants;
import com.algo4chris.algo4chrisdal.models.Member;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.user.UserException;
import com.algo4chris.algo4chriscommon.utils.IpUtils;
import com.algo4chris.algo4chriscommon.utils.SessionUtils;
import com.algo4chris.algo4chrisdal.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    ObjectMapper objectMapper;

    @Resource
    UserRepository userRepository;

    @Resource
    SessionUtils sessionUtils;

    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry
                .addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        if (request.getRequestURI().startsWith("/error")) {
                            return false;
                        }
                        log.info(" IP來源:{}: 請求路徑:{}?{}", IpUtils.getIpAddr(request),
                                                            request.getRequestURI(),
                                                            request.getQueryString());
                        SessionEntity sessionEntity = sessionUtils.pullSessionFromRequest(request);
                        String userName = sessionEntity.getUserName();
                        Member member = userRepository.findByMemberName(userName)
                                .orElseThrow(()->new UserException(MgrResponseCode.MEMBER_NOT_FOUND,new Object[]{userName}));
                        sessionEntity.setUserId(member.getId());
                        if(StringUtils.isBlank(sessionEntity.getIp())) {
                            sessionEntity.setIp("0.0.0.0");
                        }
                        request.setAttribute(SessionConstants.SESSION_ATTRIBUTE,sessionEntity);
                        return true;
                    }
                })
                // -- swagger start --
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-resources")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/css/**")
                // -- swagger end --
                .excludePathPatterns("/api/auth/login")
                .excludePathPatterns("/api/auth/signup")
                .excludePathPatterns("/api/auth/refreshToken")
                .excludePathPatterns("/login")
                .excludePathPatterns("/index")
                .excludePathPatterns("/inner/session")
                .excludePathPatterns("/tg/receive")
                .excludePathPatterns("/websocket/**")
                .addPathPatterns("/**");
    }

}
