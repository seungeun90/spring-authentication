package com.spring.auth.aggregate.jwt.store.jpa.jpe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name="OAUTH2_AUTHORIZED_CLIENT")
public class AuthorizedClientJpe {

    @EmbeddedId
    private ClientPK clientPk;

    @Column(name="ACCESS_TOKEN_TYPE")
    private String accessTokenType;

    @Column(name="ACCESS_TOKEN_VALUE",length = 4000)
    private String accessTokenValue;

    @Column(name="ACCESS_TOKEN_ISSUED_AT")
    private Instant accessTokenIssuedAt;

    @Column(name="ACCESS_TOKEN_EXPIRES_AT")
    private Instant accessTokenExpiresAt;

    @Column(name="ACCESS_TOKEN_SCOPES")
    private String accessTokenScopes;

    @Column(name="REFRESH_TOKEN_VALUE",length = 4000)
    private String refreshTokenValue;

    @Column(name="REFRESH_TOKEN_ISSUED_AT")
    private Instant refreshTokenIssuedAt;

    @Column(name="CREATED_AT")
    private Instant createdAt;



}
