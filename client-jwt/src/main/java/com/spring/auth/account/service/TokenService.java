package com.spring.auth.account.service;


import com.spring.auth.domain.jwt.TokenDto;

import java.util.List;

public interface TokenService {

    TokenDto generate(String email, String deviceId, String deviceType, String joinType);
    void expireRefreshToken(String deviceId);
    boolean validateToken(String token);
    String getSubject(String token);
    String getJoinType(String token);
    String getDeviceId(String token);
    List<String> retrieveDevices(String email);
}
