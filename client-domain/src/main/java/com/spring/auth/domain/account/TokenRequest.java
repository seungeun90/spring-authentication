package com.spring.auth.domain.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {

    private String email;
    private String deviceId;
    private String deviceType;
    private String joinType;

    public TokenRequest(String deviceId) {
        this.deviceId = deviceId;
    }
    public TokenRequest(String deviceId, String deviceType) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }
}
