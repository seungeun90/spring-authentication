package com.spring.auth.aggregate.email.service;

public interface VerificationCodeService {

    void save(String email, String code);

    boolean validate(String email, String code);
}
