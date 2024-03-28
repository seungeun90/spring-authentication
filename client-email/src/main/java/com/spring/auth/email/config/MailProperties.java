package com.spring.auth.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    private String host;
    private String port;
    private String username;
    private String password;
    private String imagePath;
    private Properties properties;

    @Getter @Setter
    public static class Properties {
        private SmtpProperties smtp;
    }

    @Getter @Setter
    public static class SmtpProperties {
        private boolean auth;
        private StarttlsProperties starttls;
        private SslProperties ssl;
    }

    @Getter @Setter
    public static class StarttlsProperties {
        private boolean enable;
    }
    @Getter @Setter
    public static class SslProperties {
        private boolean enable;
        private String protocol;

    }
}
