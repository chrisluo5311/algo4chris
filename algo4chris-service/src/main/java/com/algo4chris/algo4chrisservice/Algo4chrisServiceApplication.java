package com.algo4chris.algo4chrisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@ConfigurationPropertiesScan
@PropertySource("classpath:system-${spring.profiles.active}.properties")
@SpringBootApplication(scanBasePackages = {"com.algo4chris.algo4chrisservice","com.algo4chris.algo4chriscommon"},
        exclude = {SecurityAutoConfiguration.class})
public class Algo4chrisServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Algo4chrisServiceApplication.class, args);
    }
}
