package com.spring.auth;

import com.spring.auth.account.config.JwtProviderProperties;
import com.spring.auth.email.config.MailProperties;
import com.spring.auth.pass.config.PassProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties({MailProperties.class, JwtProviderProperties.class, PassProperties.class})
@ComponentScan(basePackages = {"com.spring.auth","다른 라이브러리 모듈", })
@SpringBootApplication
public class SpringClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringClientApplication.class, args);
	}

}
