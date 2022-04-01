package com.algo4chris.algo4chrisweb.encodetest;

import com.algo4chris.algo4chrisweb.AlgoBaseTest;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.netty.handler.codec.base64.Base64Decoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base64 Encode Test測試類<br>
 *
 * @author chris
 * */
@EnabledIfSystemProperty(named = "algo.test",matches = "true")
@DisplayName("[Base64 Encode Test]测试")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EncodeTest extends AlgoBaseTest {
    @Override
    public String userName() {
        return null;
    }

    @Override
    public String sellerName() {
        return null;
    }

    @Test
    @DisplayName("╯°□°）╯ 產生Base64 Encode")
    public void encodeSession_Test() throws Exception {
        System.out.println("開始[產生Base64 Encode]测试:");
        String secretKey = "test";
        String writeValueAsString = objectMapper.writeValueAsString(secretKey);
        byte[] encode = Base64.getEncoder().encode(writeValueAsString.getBytes());
        String encodeKey = new String(encode);
        System.out.println("[Base64 Encode]結果: "+ encodeKey);

        //decode
        byte[] decode = Base64.getDecoder().decode(encodeKey);
        String decodeKey = new String(decode);
        System.out.println("[Base64 Decode]結果: "+ decodeKey);
    }

}
