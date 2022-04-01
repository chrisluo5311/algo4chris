package com.algo4chris.algo4chrisweb.security.jwt;

import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.utils.IpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        String ip = IpUtils.getIpAddr(request);
        log.error("【ClientAccessDeniedHandler】【ip】:{} 禁用信息: {} 請求路徑uri: {}?{}",ip,
                                                                                       e.getMessage(),
                                                                                       request.getRequestURI(),
                                                                                       request.getQueryString());
        //response body 用户状态为0时为禁用 显示账号状态为禁用，请联系客服处理
        final Map<String, Object> disabledBody = new HashMap<>();
        disabledBody.put("code", MgrResponseCode.USER_DISABLED.getCode());
        disabledBody.put("message",MgrResponseCode.USER_DISABLED.getMessage());
        disabledBody.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), disabledBody);
    }
}
