package com.spring.auth.client.logic;

import com.spring.auth.domain.account.AccountResponse;
import com.spring.auth.domain.oauth.AccountOAuthUser;
import com.spring.auth.domain.oauth.KakaoOAuthUser;
import com.spring.auth.proxy.service.UserProxyService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MobileOAuthUserLogic {
    private UserProxyService userProxyService;
    private ClientRegistrationRepository clientRegistrationRepository;
    private RestOperations restOperations;

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    public MobileOAuthUserLogic(
            UserProxyService userProxyService,
      //      MobileAuthorizedClientService mobileAuthorizedClientService,
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        this.userProxyService = userProxyService;
    //    this.mobileAuthorizedClientService = mobileAuthorizedClientService;
        this.clientRegistrationRepository= clientRegistrationRepository;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    public AccountResponse loadUser(OAuth2AccessToken accessToken) throws OAuth2AuthenticationException {

        /**
         * 카카오 유저 정보 조회
         * */
        RequestEntity<?> request = this.convert(accessToken);
        ResponseEntity<Map<String, Object>> response = getResponse(request);
        Map<String, Object> userAttributes = response.getBody();
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(userAttributes));

        for (String authority : accessToken.getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
        }
        OAuth2User oAuth2User = new DefaultOAuth2User(authorities, userAttributes, "id");
        Map<String, Object> attributes = oAuth2User.getAttributes();

        AccountOAuthUser user = new KakaoOAuthUser(attributes);


        return new AccountResponse(user.getEmail(), user.getEmail(), user.getMobile());
    }

    private ResponseEntity<Map<String, Object>> getResponse(RequestEntity<?> request) {
        try {
            return this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        }
        catch (OAuth2AuthorizationException ex) {
            throw ex;
        }
    }
    private RequestEntity<?> convert(OAuth2AccessToken token) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao") ;
        HttpMethod httpMethod = HttpMethod.GET;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        URI uri = UriComponentsBuilder
                .fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .build()
                .toUri();

        headers.setBearerAuth(token.getTokenValue());
        RequestEntity<?> request = new RequestEntity<>(headers, httpMethod, uri);
        return request;
    }
}
