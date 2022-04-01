package com.algo4chris.algo4chrisweb;

import com.algo4chris.algo4chrisdal.session.SessionEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * 逻辑层
 *
 * */
@Slf4j
public abstract class AlgoBaseTest extends AlgoTestUtil {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Resource
    protected RedisTemplate<String, String> redisTemplate;

    /** 登入账号  */
    public abstract String userName();
    /** 賣家帳號 */
    public abstract String sellerName();

    @BeforeEach
    public void setup(){
        printDivider("開始測試");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void afterTest(){
        printDivider("结束測試");
    }

    /**
     * 制作Headers
     * */
    public String getHeaders(){
        SessionEntity sessionEntity = new SessionEntity();
        if(userName()!=null){
            sessionEntity.setUserName(userName());
            return encodeSessionEntity(sessionEntity);
        }
        return null;
    }


}
