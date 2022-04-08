package com.algo4chris.algo4chrisweb.security.jwt;

import com.algo4chris.algo4chriscommon.common.constant.HttpExceptionConst;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.utils.IpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthEntryPointJWT implements AuthenticationEntryPoint {

    @Resource
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String ip = IpUtils.getIpAddr(request);
        log.error("【AuthEntryPointJwt】未授权错误信习: {} 請求路徑uri: {}?{}",authException.getMessage(),
                                                                           request.getRequestURI(),
                                                                           request.getQueryString());

        //超時回應006
        if(request.getAttribute(HttpExceptionConst.JWT_EXPIRED_CODE_KEY)!=null){
            log.error("【ip】:{} Jwt token 已超时，需重新登入",ip);
            //response body
            final Map<String, Object> body = new LinkedHashMap<>();
            body.put("code", MgrResponseCode.JWT_TOKEN_EXPIRED.getCode());
            body.put("message", MgrResponseCode.JWT_TOKEN_EXPIRED.getMessage());
            body.put("error", "Token超時");
            body.put("path", request.getServletPath());
            objectMapper.writeValue(response.getOutputStream(), body);
            return;
        }

        if(request.getAttribute(HttpExceptionConst.API_OUT_OF_LIMIT)!=null){
            log.error("【ip】:{} Api rate exceeds limit",ip);
            //response body
            //例:{"path":"/api/getOnlineMember","code":429,"message":"Too Many Requests"}
            final Map<String, Object> apiOutOfLimit = new HashMap<>();
            apiOutOfLimit.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
            apiOutOfLimit.put("message",HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
            apiOutOfLimit.put("error", "Too Many Requests");
            apiOutOfLimit.put("path", request.getServletPath());
            objectMapper.writeValue(response.getOutputStream(), apiOutOfLimit);
//            response.sendRedirect("/403.html");
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //response body
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("message", authException.getMessage());
        body.put("error", "未經授權");
        body.put("path", request.getServletPath());
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
