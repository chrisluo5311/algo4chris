package com.algo4chris.algo4chriscommon.utils;

import com.algo4chris.algo4chriscommon.common.constant.SessionConstants;
import com.algo4chris.algo4chriscommon.utils.IpUtils;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.user.UserJwtException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 操作 SessionEntity 的工具類
 *
 * @author chris
 * @date 2022/01/30
 * */
@Component
@Slf4j
public class SessionUtils {

    @Resource
    ObjectMapper objectMapper;

    /**
     * 從 HttpServletRequest 中取出 SessionEntity
     *
     * @param servletRequest HttpServletRequest
     * @return SessionEntity
     * @throws JsonProcessingException
     * */
    public SessionEntity pullSessionFromRequest(HttpServletRequest servletRequest) throws JsonProcessingException, UserJwtException {
        String encodedJson = servletRequest.getHeader(SessionConstants.SESSION_ATTRIBUTE_KEY);
        if (StringUtils.isBlank(encodedJson)) {
            log.warn("請求來源未带有token 請求ip位址: {}", IpUtils.getIpAddr(servletRequest));
            throw new UserJwtException(MgrResponseCode.REQUEST_WITHOUT_TOKEN,null);
        }
        String json = new String(Base64.getDecoder().decode(encodedJson));
        return objectMapper.readValue(json, SessionEntity.class);
    }

    /**
     * 在 HttpServletRequest 中放置 SessionEntity
     *
     * @param entity SessionEntity
     * @param request HttpServletRequest
     * @return SessionEntity
     * */
    public void pushSessionToRequest(SessionEntity entity, HttpServletRequest request) {
        String encodedSession = encodeSessionEntity(entity);
        request.setAttribute(SessionConstants.SESSION_ATTRIBUTE_KEY, encodedSession);
    }

    /**
     * 將 sessionEntity 轉乘 json 再進行 Base64 encode
     *
     * @param sessionEntity SessionEntity
     * @return base64後的sessionEntity
     * */
    private String encodeSessionEntity(SessionEntity sessionEntity) {
        try {
            String json = objectMapper.writeValueAsString(sessionEntity);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
