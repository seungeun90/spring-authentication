package com.spring.auth.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.account.service.TokenService;
import com.spring.auth.client.filter.JwtAuthenticationFilter;
import com.spring.auth.client.filter.JwtAuthorizationFilter;
import com.spring.auth.client.handler.CustomAuthenticationFailureHandler;
import com.spring.auth.client.handler.CustomAuthenticationProvider;
import com.spring.auth.client.handler.CustomAuthenticationSuccessHandler;
import com.spring.auth.client.logic.CustomUserDetailLogic;
import com.spring.auth.client.logic.MobileOAuthUserLogic;
import com.spring.auth.proxy.service.UserProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2ClientConfig {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomUserDetailLogic customUserDetailService;
    private final TokenService tokenService;
   // private final MobileAuthorizedClientService mobileAuthorizedClientService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final UserProxyService userProxyLogic;

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/account/logout","/kakao/user","/pass/user","/email","/email/code").permitAll()
                        .anyRequest().authenticated()

                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestResolver(
                                        authorizationRequestResolver(this.clientRegistrationRepository)
                                )
                        )
                        .successHandler(successHandler)
                        .failureHandler(new CustomAuthenticationFailureHandler())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(tokenService, customUserDetailService, objectMapper), OAuth2AuthorizationRequestRedirectFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(
                        authenticationManager(),
                        tokenService, objectMapper), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository, "/oauth2/authorization");
        return  authorizationRequestResolver;
    }

    @Bean
    public MobileOAuthUserLogic mobileOAuthUserService(){
        return new MobileOAuthUserLogic(
                userProxyLogic,
             //   mobileAuthorizedClientService,
                clientRegistrationRepository );
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        CustomAuthenticationProvider authenticationProvider =
                new CustomAuthenticationProvider(customUserDetailService,
                        passwordEncoder  );
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
