package com.spring.auth.aggregate.jwt.store.redis;

import com.spring.auth.aggregate.jwt.store.redis.jpe.TokenJpe;

import java.util.List;

public interface TokenStore {
    void saveRefreshToken(TokenJpe jpe);
    void deleteRefreshTokenByDeviceId(String deviceId);
    boolean existsByDeviceIdAndRefreshToken(String deviceId, String token);
    List<String> retrieveDevices(String email);
    TokenJpe retrieveTokenByDeviceId(String deviceId);
}
