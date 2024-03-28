package com.spring.auth.client.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //private final JwtProvider jwtProvider;
    private final DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        List<String> roles = oAuth2User.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
    /*    String token = jwtProvider.createToken(email, roles);
        Cookie accessTokenCookie = new Cookie("access_token", token);
        accessTokenCookie.setHttpOnly(false);
     //   accessTokenCookie.setMaxAge(60 * 60); // 토큰의 만료 시간 설정 (예: 1시간)
        accessTokenCookie.setPath("/"); // 쿠키의 유효 경로 설정
        response.addCookie(accessTokenCookie);

        // 홈 페이지로 리다이렉션
        response.sendRedirect("/");*/
    }
}
