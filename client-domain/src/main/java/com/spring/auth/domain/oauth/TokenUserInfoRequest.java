package com.spring.auth.domain.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenUserInfoRequest {
    private String tokenValue;
    private Instant issuedAt;
    private Instant expiresAt;
    private String deviceId;
    private String deviceType;
}
