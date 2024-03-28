package com.spring.auth.pass.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "pass")
public class PassProperties {
    private String clientPrefix;
    private String usageCode;
    private String serviceType;
    private String retTransferType;
    private String targetUri;
    private String redirectUri;
    private String keyFilePath;
    private String keyFileCode;

}
