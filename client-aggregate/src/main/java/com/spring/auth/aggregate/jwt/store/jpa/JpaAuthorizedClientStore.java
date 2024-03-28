package com.spring.auth.aggregate.jwt.store.jpa;

import com.spring.auth.aggregate.jwt.store.jpa.repository.JpaAuthorizedClientRepository;
import com.spring.auth.aggregate.jwt.store.jpa.jpe.AuthorizedClientJpe;
import com.spring.auth.aggregate.jwt.store.jpa.jpe.ClientPK;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JpaAuthorizedClientStore implements OAuth2AuthorizedClientService {
    private final JpaAuthorizedClientRepository clientRegistrationRepository;
    private final ClientRegistrationRepository registrationRepository;


    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        AuthorizedClientJpe authorizedClientJpe = clientRegistrationRepository.findByClientPkClientRegistrationIdAndClientPkPrincipalName(clientRegistrationId, principalName);
        ClientRegistration clientRegistration = this.registrationRepository
                .findByRegistrationId(clientRegistrationId);
        if (clientRegistration == null) {
            throw new DataRetrievalFailureException(
                    "The ClientRegistration with id '" + clientRegistrationId + "' exists in the data source, "
                            + "however, it was not found in the ClientRegistrationRepository.");
        }
        OAuth2AccessToken.TokenType tokenType = null;
        String accessTokenType = authorizedClientJpe.getAccessTokenType();
        if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(accessTokenType)) {
            tokenType = OAuth2AccessToken.TokenType.BEARER;
        }
        String tokenValue = authorizedClientJpe.getAccessTokenValue();
        Instant issuedAt = authorizedClientJpe.getAccessTokenIssuedAt();
        Instant expiresAt = authorizedClientJpe.getAccessTokenExpiresAt();
        Set<String> scopes = Collections.emptySet();
        String accessTokenScopes = authorizedClientJpe.getAccessTokenScopes();
        if (accessTokenScopes != null) {
            scopes = StringUtils.commaDelimitedListToSet(accessTokenScopes);
        }
        OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, tokenValue, issuedAt, expiresAt, scopes);

        String refreshTokenValue = authorizedClientJpe.getRefreshTokenValue();
        Instant refreshTokenIssuedAt = authorizedClientJpe.getRefreshTokenIssuedAt();

        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue, refreshTokenIssuedAt);

        return (T) new OAuth2AuthorizedClient(clientRegistration, principalName, accessToken, refreshToken);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();

        AuthorizedClientJpe authorizedClientJpe = new AuthorizedClientJpe();
        authorizedClientJpe.setClientPk(new ClientPK(clientRegistration.getRegistrationId(), principal.getName()));
        authorizedClientJpe.setAccessTokenType(accessToken.getTokenType().getValue());
        authorizedClientJpe.setAccessTokenValue(accessToken.getTokenValue());
        authorizedClientJpe.setAccessTokenIssuedAt(accessToken.getIssuedAt());
        authorizedClientJpe.setAccessTokenExpiresAt(accessToken.getExpiresAt());
        authorizedClientJpe.setAccessTokenScopes(StringUtils.collectionToDelimitedString(accessToken.getScopes(), ","));
        authorizedClientJpe.setRefreshTokenValue(refreshToken.getTokenValue());
        authorizedClientJpe.setRefreshTokenIssuedAt(refreshToken.getIssuedAt());
        authorizedClientJpe.setCreatedAt(Instant.now());
        clientRegistrationRepository.save(authorizedClientJpe);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        clientRegistrationRepository.deleteByClientPkClientRegistrationIdAndClientPkPrincipalName(clientRegistrationId, principalName);
    }
}
