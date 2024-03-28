package com.spring.auth.aggregate.jwt.store.redis;

import com.spring.auth.aggregate.jwt.store.redis.jpe.TokenJpe;
import com.spring.auth.aggregate.jwt.store.redis.repository.RedisJwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TokenRedisStore implements TokenStore {

    private final RedisJwtRepository jwtRepository;

    public void saveRefreshToken(TokenJpe jpe){
        jwtRepository.save(jpe);
    }

    public void deleteRefreshTokenByDeviceId(String deviceId){
        jwtRepository.deleteById(deviceId);
    }


    public boolean existsByDeviceIdAndRefreshToken(String deviceId, String token){
        Optional<TokenJpe> jpe = jwtRepository.findById(deviceId);
        if(jpe.isEmpty()) return false;
        TokenJpe tokenJpe = jpe.get();
        return tokenJpe.getRefreshToken().equals(token);
    }

    public List<String> retrieveDevices(String email){
        List<TokenJpe> list = jwtRepository.findByEmail(email);
        return list.stream().map(jpe -> jpe.getDeviceId()).collect(Collectors.toList());
    }

    public TokenJpe retrieveTokenByDeviceId(String deviceId){
        TokenJpe tokenJpe = jwtRepository.findById(deviceId).orElse(null);
        return tokenJpe;
    }
}
