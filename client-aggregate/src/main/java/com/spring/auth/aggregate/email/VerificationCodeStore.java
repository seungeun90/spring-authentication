package com.spring.auth.aggregate.email;


import com.spring.auth.aggregate.email.jpe.VerificationCodeJpe;

public interface VerificationCodeStore {
    void saveVerificationCode(VerificationCodeJpe jpe);

    VerificationCodeJpe retrieveVerificationCode(String email);
}
