package com.algo4chris.algo4chrisweb;

import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 工具層
 *
 * */
public abstract class AlgoTestUtil {

    protected static final String BestPay_Session = "BestPay-Session";



    @Resource
    protected ObjectMapper objectMapper;

    protected String encodeSessionEntity(SessionEntity sessionEntity) {
        try {
            String json = objectMapper.writeValueAsString(sessionEntity);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    protected void printDivider(String msg) {
        System.out.println("===================================" + msg + "===================================");
    }

}
