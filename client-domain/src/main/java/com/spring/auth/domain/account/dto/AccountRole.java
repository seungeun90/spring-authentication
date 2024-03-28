package com.spring.auth.domain.account.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountRole {
    ADMIN,
    USER;
    private static final String PREFIX = "ROLE_";
    public String getAuthority(){
        return PREFIX + this.name();
    }
}
