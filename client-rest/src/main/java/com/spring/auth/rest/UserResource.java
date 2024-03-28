package com.spring.auth.rest;

import com.spring.auth.account.service.TokenService;
import com.spring.auth.client.logic.MobileOAuthUserLogic;
import com.spring.auth.domain.account.AccountResponse;
import com.spring.auth.domain.jwt.TokenDto;
import com.spring.auth.domain.oauth.TokenUserInfoRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserResource {
    private final MobileOAuthUserLogic mobileOAuthUserService;

    private final TokenService tokenService;

    /**
     * 모바일에서 카카오 인증을 통해 전달받은 access token 으로
     * 유저 정보 조회 및 통합계정 생성
     * JWT 발행
     * */
    @PostMapping("/kakao/user")
    public ResponseEntity<?> getUserInfo(@RequestBody RequestData<TokenUserInfoRequest> request, HttpServletResponse response) {
        TokenUserInfoRequest data = request.getData();

        /**
         * 계정 확인
         * */
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                data.getTokenValue(), data.getIssuedAt(), data.getExpiresAt());
        AccountResponse oAuth2User = mobileOAuthUserService.loadUser(oAuth2AccessToken);

        /**
         * 토큰 발행
         * */
        TokenDto token = tokenService.generate(oAuth2User.getAccountEmail(), data.getDeviceId(), data.getDeviceType(),"sns");

        Cookie accessCookie = new Cookie("access_token", token.getAccessToken().getToken());
        Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken().getToken());
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        return ResponseData.success(oAuth2User);
    }

}
