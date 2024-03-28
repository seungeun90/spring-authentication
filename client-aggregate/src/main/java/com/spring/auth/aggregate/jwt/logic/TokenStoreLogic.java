package com.spring.auth.aggregate.jwt.logic;

import com.spring.auth.aggregate.jwt.store.redis.TokenStore;
import com.spring.auth.aggregate.jwt.store.redis.jpe.TokenJpe;
import com.spring.auth.aggregate.jwt.service.TokenStoreService;
import com.spring.auth.domain.jwt.Token;
import com.spring.auth.domain.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenStoreLogic implements TokenStoreService {
    private final TokenStore tokenStore;
    @Override
    public TokenDto retrieveTokenByDeviceId(String deviceId) {
        return null;
    }

    @Override
    public void save(String deviceId, String email, Token token) {

        TokenJpe tokenJpe = tokenStore.retrieveTokenByDeviceId(deviceId);
        if(tokenJpe == null) {
            tokenJpe = new TokenJpe(deviceId, email, token.getToken());
        } else {
            tokenJpe.setRefreshToken(token.getToken());
        }
        tokenStore.saveRefreshToken(tokenJpe);
    }

    @Override
    public void deleteRefreshTokenByDeviceId(String deviceId) {
        tokenStore.deleteRefreshTokenByDeviceId(deviceId);
    }

    @Override
    public boolean existsByDeviceIdAndRefreshToken(String deviceId, String token) {
        return tokenStore.existsByDeviceIdAndRefreshToken(deviceId, token);
    }

    @Override
    public List<String> retrieveDevices(String email) {
        return  tokenStore.retrieveDevices(email);
    }
}
