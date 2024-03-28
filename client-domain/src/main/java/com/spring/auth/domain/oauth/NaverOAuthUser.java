package com.spring.auth.domain.oauth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class NaverOAuthUser implements AccountOAuthUser {

    private Map<String, Object> attributes;
    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getMobile() {
        return (String) attributes.get("mobile");
    }

    @Override
    public String getCI() {
        return null;
    }

    @Override
    public String getBirthDay() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
