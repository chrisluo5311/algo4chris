package com.algo4chris.algo4chrisweb.properties_test;

import com.algo4chris.algo4chrisweb.AlgoBaseTest;
import com.algo4chris.algo4chrisweb.config.AlgoProperties;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Properties;
import com.algo4chris.algo4chriscommon.common.constant.InnerRouteConst;

/**
 * Properties Test測試類<br>
 * ex.
 * system-default.properties
 *
 * @author chris
 * */
@EnabledIfSystemProperty(named = "algo.test",matches = "true")
@DisplayName("[Properties Test]测试")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PropertyTest extends AlgoBaseTest {

    @Override
    public String userName() {
        return null;
    }

    @Override
    public String sellerName() {
        return null;
    }


    @Test
    @DisplayName("╯°□°）╯ 測試Properties有無注入")
    public void property_Test() throws Exception {
        System.out.println("開始[Properties]测试:");

        System.out.println("system.version: "+ AlgoProperties.version);

        Properties prop = Environment.getProperties();
        System.out.println("google client-id: "
                + prop.getProperty("spring.security.oauth2.client.registration.google.client-id"));

    }

    @Test
    @DisplayName("╯°□°）╯ 測試InnerRouteConst有無注入")
    public void innerRouteConst_Test() throws Exception {
        System.out.println("開始[InnerRouteConst]测试:");

//        System.out.println("port: "+ InnerRouteConst.port);


    }
}
