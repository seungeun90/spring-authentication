package com.spring.auth.domain.oauth;

import java.util.Map;

public interface AccountOAuthUser {

    Map<String, Object> getAttributes();
    String getProvider();
    String getEmail();
    String getName();
    String getMobile();

    String getCI();

    String getBirthDay();
    String getGender();
}
