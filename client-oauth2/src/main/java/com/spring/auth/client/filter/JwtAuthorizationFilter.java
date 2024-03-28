package com.spring.auth.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.account.service.TokenService;
import com.spring.auth.client.handler.CustomAuthenticationFailureHandler;
import com.spring.auth.domain.account.User;
import com.spring.auth.domain.jwt.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 로그인 시 계정 검증 후
 * JWT 발행
 * */
public class JwtAuthorizationFilter extends AbstractAuthenticationProcessingFilter {
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private ObjectMapper objectMapper;
    public static final String DEFAULT_ANT_PATH_REQUEST_MATCHER = "/account/login";

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "email";

    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                     TokenService tokenService,
                                  ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
        Map<String, String> map = new HashMap<>();
        map.put("message", failed.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(map);
        // 응답을 종료
        httpResponse.getWriter().write(jsonResponse);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return authentication;
    }

    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User principal = (User)authResult.getPrincipal();
        String deviceId = request.getParameter("deviceId");
        String deviceType = request.getParameter("deviceType");
        TokenDto token = tokenService.generate(principal.getEmail(), deviceId, deviceType,"email");

        Cookie accessCookie = new Cookie("access_token", token.getAccessToken().getToken());
        Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken().getToken());
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        response.getWriter().flush();
    }

}
