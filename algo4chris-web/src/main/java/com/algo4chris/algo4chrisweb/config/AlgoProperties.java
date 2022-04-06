package com.algo4chris.algo4chrisweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "system")
public class AlgoProperties {

    /** system.version */
    public static String version;

    /** 進入service的host */
    public static String serviceHost;

    /** 忽略資源的url */
    public static List<String> resourceUrls;

    /** 忽略api的url */
    public static List<String> apiIgnoredUrls;

    public AlgoProperties(String version,String serviceHost,List<String> resourceUrls,List<String> apiIgnoredUrls) {
        AlgoProperties.version = version;
        AlgoProperties.serviceHost = serviceHost;
        AlgoProperties.resourceUrls = resourceUrls;
        AlgoProperties.apiIgnoredUrls = apiIgnoredUrls;
    }

}
