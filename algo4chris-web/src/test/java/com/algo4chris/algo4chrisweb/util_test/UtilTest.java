package com.algo4chris.algo4chrisweb.util_test;

import com.algo4chris.algo4chriscommon.utils.TranslatorUtil;
import com.algo4chris.algo4chrisweb.AlgoBaseTest;
import com.algo4chris.algo4chrisweb.config.AlgoProperties;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/**
 * Common包下的Util Test測試類<br>
 *
 * @author chris
 * */
@EnabledIfSystemProperty(named = "algo.test",matches = "true")
@DisplayName("[UtilTest]测试")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UtilTest extends AlgoBaseTest {

    @Override
    public String userName() {
        return null;
    }

    @Override
    public String sellerName() {
        return null;
    }


    @Test
    @DisplayName("╯°□°）╯ UtilTest")
    public void property_Test() throws Exception {
        System.out.println("開始[UtilTest]测试:");

        String transWord = TranslatorUtil.translate("Emergency workers at the site are still unable to pinpoint the cause of the explosion.");

        System.out.println("[Api 翻譯]結果: " + transWord);
        printDivider("");
        System.out.println("應展現結果: " + "現場的急救人員仍然無法確定爆炸的起因。");
    }
}
