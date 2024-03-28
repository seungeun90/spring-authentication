package com.spring.auth.domain.oauth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class KakaoOAuthUser implements AccountOAuthUser {

    private Map<String, Object> attributes;
    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) getAccountInfo().get("email");
    }

    @Override
    public String getName() {
        return (String) getAccountInfo().get("name");
    }

    @Override
    public String getMobile() {
        return (String) getAccountInfo().get("phone_number");
    }

    @Override
    public String getCI() {
        return (String) getAccountInfo().get("account_ci");
    }

    @Override
    public String getBirthDay() {
        return
                (String) getAccountInfo().get("birthyear") +
                "."+
                (String) getAccountInfo().get("birthday");
    }

    @Override
    public String getGender() {
        return (String) getAccountInfo().get("gender");
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Map<String, Object> getAccountInfo() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }
}
