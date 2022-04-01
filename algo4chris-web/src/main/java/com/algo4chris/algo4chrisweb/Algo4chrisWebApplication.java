package com.algo4chris.algo4chrisweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * swagger文檔地址: http://localhost:8080/doc.html
 * 首頁地址: http://localhost:8080/
 *
 * */
@EnableWebMvc
@ConfigurationPropertiesScan
@PropertySource("classpath:system-${spring.profiles.active}.properties")
@SpringBootApplication(scanBasePackages = {"com.algo4chris.algo4chrisdal.repository","com.algo4chris.algo4chrisweb.init"})
public class Algo4chrisWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Algo4chrisWebApplication.class, args);
    }

}
