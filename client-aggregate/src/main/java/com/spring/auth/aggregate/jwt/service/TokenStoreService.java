package com.spring.auth.aggregate.jwt.service;

import com.spring.auth.domain.jwt.Token;
import com.spring.auth.domain.jwt.TokenDto;

import java.util.List;

public interface TokenStoreService {

    TokenDto retrieveTokenByDeviceId(String deviceId);

    void save(String deviceId, String email, Token token);

    void deleteRefreshTokenByDeviceId(String deviceId);
    boolean existsByDeviceIdAndRefreshToken(String deviceId, String token);

    List<String> retrieveDevices(String email);

}
