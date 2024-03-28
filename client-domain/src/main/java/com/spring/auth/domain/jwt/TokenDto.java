package com.spring.auth.domain.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TokenDto {
    private Token accessToken;
    private Token refreshToken;
}
