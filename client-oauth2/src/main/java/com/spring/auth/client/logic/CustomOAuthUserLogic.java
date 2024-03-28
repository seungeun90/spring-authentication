package com.spring.auth.client.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.domain.oauth.AccountOAuthUser;
import com.spring.auth.domain.oauth.KakaoOAuthUser;
import com.spring.auth.domain.oauth.NaverOAuthUser;
import com.spring.auth.proxy.service.UserProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuthUserLogic extends DefaultOAuth2UserService {
    private final ObjectMapper objectMapper;
    private final UserProxyService userProxyLogic;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        AccountOAuthUser user;
        if (registrationId.equals("naver")) {
             user = new NaverOAuthUser((Map)attributes.get("response"));
        } else {
            user = new KakaoOAuthUser(attributes);
        }

        Account account = userProxyLogic.getUser(user.getEmail());
        if (account == null) {
            account = Account.builder()
                    .email(user.getEmail())
                    .mobile(user.getMobile())
                    .name(user.getName())
                    .build();
            userProxyLogic.save(account);
        }
        Map<String, Object> oauthAccount = objectMapper.convertValue(account, Map.class);

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), oauthAccount, "email");

    }
}
