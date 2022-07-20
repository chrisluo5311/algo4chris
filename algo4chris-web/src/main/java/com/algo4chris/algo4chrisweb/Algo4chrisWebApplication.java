package com.algo4chris.algo4chrisweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

/**
 * swagger文檔地址: http://localhost:8080/doc.html<br>
 * 首頁地址: http://localhost:8080/<br>
 *
 * */
@ConfigurationPropertiesScan
@PropertySource("classpath:system-${spring.profiles.active}.properties")
@SpringBootApplication(scanBasePackages = {"com.algo4chris.algo4chrisweb","com.algo4chris.algo4chriscommon"},
                       exclude = {SecurityAutoConfiguration.class})
public class Algo4chrisWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Algo4chrisWebApplication.class, args);
    }


}
