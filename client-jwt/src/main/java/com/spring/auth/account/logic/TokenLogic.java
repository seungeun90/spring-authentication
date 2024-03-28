package com.spring.auth.account.logic;

import com.spring.auth.account.config.JwtProviderProperties;
import com.spring.auth.account.service.TokenService;
import com.spring.auth.aggregate.jwt.service.TokenStoreService;
import com.spring.auth.domain.jwt.DeviceType;
import com.spring.auth.domain.jwt.Token;
import com.spring.auth.domain.jwt.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TokenLogic implements TokenService {
    private final TokenStoreService tokenStoreService;
    private final JwtProviderProperties properties;
    private static final String DEVICE_KEY = "device";
    private static final String ACCOUNT_KEY = "type";

    @Transactional
    public TokenDto generate(String email, String deviceId, String deviceType, String joinType) {
        if(!StringUtils.hasText(deviceId)) throw new RuntimeException("device Id 가 없습니다.");
        Token accessToken = generateAccessToken(email, deviceId, deviceType, joinType);
        Token refreshToken = generateRefreshToken(email, deviceId, deviceType, joinType);
        tokenStoreService.save(deviceId, email, refreshToken);
        return new TokenDto(accessToken, refreshToken);
    }

    @Override
    public void expireRefreshToken(String deviceId) {
        tokenStoreService.deleteRefreshTokenByDeviceId(deviceId);
    }

    public boolean validateToken(String token) {
        if(isTokenExpired(token)) throw new RuntimeException("expired token");
        String deviceId = String.valueOf(getClaims(token).get(DEVICE_KEY));
        if(!tokenStoreService.existsByDeviceIdAndRefreshToken(deviceId, token)) return false;
        return true;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> retrieveDevices(String email) {
        return tokenStoreService.retrieveDevices(email);
    }

    private boolean isTokenExpired(String token){
        Date expirationDate =null;
        try {
            expirationDate = getClaims(token).getExpiration();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("만료된 토큰입니다");
        }

        return expirationDate.before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(properties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getJoinType(String token){
        return (String) getClaims(token).get(ACCOUNT_KEY);
    }
    public String getDeviceId(String token){
        return (String) getClaims(token).get(DEVICE_KEY);
    }
    private Map<String, Object> setClaim(String deviceId, String joinType){
        Map<String, Object> map = new HashMap<>();
        map.put(DEVICE_KEY, deviceId);
        map.put(ACCOUNT_KEY, joinType);
        return map;
    }

    private Token generateAccessToken(String email, String deviceId, String deviceType, String joinType) {

        String token = Jwts.builder()
                .setClaims(setClaim(deviceId, joinType))
                .setIssuer("spring-oauth")
                .setSubject(email)
                .setExpiration(calculateExpiration(deviceType, "access"))
                .signWith(SignatureAlgorithm.HS256, properties.getSecretKey())
                .compact();
        return new Token(token);
    }

    private Token generateRefreshToken(String email, String deviceId, String deviceType, String joinType) {

        String token = Jwts.builder()
                .setClaims(setClaim(deviceId, joinType))
                .setIssuer("spring-oauth")
                .setSubject(email)
                .setExpiration(calculateExpiration(deviceType, "refresh"))
                .signWith(SignatureAlgorithm.HS256, properties.getSecretKey())
                .compact();
        return new Token(token);
    }

    private Date calculateExpiration(String deviceType, String tokenType){
        Instant instant = Instant.now().truncatedTo(ChronoUnit.MICROS);
        if("access".equals(tokenType)){
            Instant expiredAt = instant.plusMillis(Long.valueOf(getAccessExpirationByDeviceType(deviceType)));
            return Date.from(expiredAt);
        }
        Instant expiredAt = instant.plusMillis(Long.valueOf(getRefreshExpirationByDeviceType(deviceType)));
        return Date.from(expiredAt);
    }

    private String getAccessExpirationByDeviceType(String deviceType){
        if(DeviceType.MOBILE.name().equals(deviceType.toUpperCase())) {
            return properties.getMobile().getAccessTokenExpiredDate();
        }
        return properties.getWeb().getAccessTokenExpiredDate();
    }
    private String getRefreshExpirationByDeviceType(String deviceType){
        if(DeviceType.MOBILE.name().equals(deviceType.toUpperCase())) {
            return properties.getMobile().getRefreshTokenExpiredDate();
        }
        return properties.getWeb().getRefreshTokenExpiredDate();
    }

}
