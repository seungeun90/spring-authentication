package com.spring.auth.account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProviderProperties {
    private String secretKey;
    private WebProperties web;
    private MobileProperties mobile;

    @Getter @Setter
    public static class WebProperties {
        private String accessTokenExpiredDate;
        private String refreshTokenExpiredDate;
    }

    @Getter @Setter
    public static class MobileProperties {
        private String accessTokenExpiredDate;
        private String refreshTokenExpiredDate;
    }




}
