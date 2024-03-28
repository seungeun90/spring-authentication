package com.spring.auth.rest;

import com.spring.auth.account.service.TokenService;
import com.spring.auth.domain.account.User;
import com.spring.auth.domain.account.TokenRequest;
import com.spring.auth.domain.jwt.TokenDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TokenResource {

    private final TokenService tokenService;

    /**
     * 로그인 시
     * access / refresh token 발행
     * */
    @PostMapping("/account/logout")
    public ResponseEntity<?> removeToken(@RequestBody RequestData<TokenRequest> request){
        TokenRequest account = request.getData();
        tokenService.expireRefreshToken(account.getDeviceId());
        return ResponseData.success(null);
    }

    /**
     * access token 만료 시
     * access token / refresh token 재발행
     * */
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RequestData<TokenRequest> request,
                                                Authentication authentication,
                                                HttpServletResponse response){
        User account = (User) authentication.getPrincipal();
        TokenRequest accountRequest = request.getData();
        TokenDto tokens = tokenService.generate(account.getEmail(), accountRequest.getDeviceId(), accountRequest.getDeviceType(), accountRequest.getJoinType());
        Cookie accessCookie = new Cookie("access_token", tokens.getAccessToken().getToken());
        Cookie refreshCookie = new Cookie("refresh_token", tokens.getRefreshToken().getToken());
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        return ResponseData.success(HttpStatus.OK);
    }

    /**
     * email로 로그인 된 device 목록 반환
     * */
    @PostMapping("/devices")
    public ResponseEntity<?> retrieveDevices(Authentication authentication){
        User account = (User) authentication.getPrincipal();
        List<String> devices = tokenService.retrieveDevices(account.getEmail());
        return ResponseData.success(devices);
    }

    /**
     * 특정 device 계정을 로그아웃 시킨다.
     * */
    @PostMapping("/device/{deviceId}")
    public ResponseEntity<?> expireRefreshToken(Authentication authentication, @PathVariable("deviceId") String deviceId) {
        tokenService.expireRefreshToken(deviceId);
        return ResponseData.success(null);
    }
}
