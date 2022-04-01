package com.algo4chris.algo4chriscommon.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static com.algo4chris.algo4chriscommon.common.constant.SerialConst.LOCAL_DATETIME_SERIALIZER;

@Configuration
@PropertySource("classpath:algo-common.properties")
public class RegisterModuleConfig {

    @Bean
    public Module javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATETIME_SERIALIZER);
        return module;
    }
}
