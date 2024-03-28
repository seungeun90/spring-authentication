package com.spring.auth.account.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProviderProperties.class})
public class JwtProviderConfiguration {
}
