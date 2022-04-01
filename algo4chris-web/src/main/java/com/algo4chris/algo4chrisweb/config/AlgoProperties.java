package com.algo4chris.algo4chrisweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "system")
public class AlgoProperties {

    /** system.version */
    public static String version;

    public AlgoProperties(String version) {
        this.version = version;
    }
}
