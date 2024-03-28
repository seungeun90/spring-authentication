package com.spring.auth.aggregate.email.logic;

import com.spring.auth.aggregate.email.VerificationCodeStore;
import com.spring.auth.aggregate.email.jpe.VerificationCodeJpe;
import com.spring.auth.aggregate.email.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationCodeLogic implements VerificationCodeService {
    private final VerificationCodeStore verificationCodeStore;

    /**
     * 1. 신규 저장
     * 2. 덮어 쓰기
     * */
    @Override
    public void save(String email, String code) {
        VerificationCodeJpe jpe = verificationCodeStore.retrieveVerificationCode(email);
        /**
         * 인증코드 유효 시간 3분
         * */
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(3);

        if(jpe!=null) {
            jpe.setCode(code);
            jpe.setExpiredAt(expiredAt);
            verificationCodeStore.saveVerificationCode(jpe);
            return;
        }


        verificationCodeStore.saveVerificationCode(new VerificationCodeJpe(email, code, expiredAt));
    }

    @Override
    public boolean validate(String email, String code) {
        VerificationCodeJpe verificationCodeJpe = verificationCodeStore.retrieveVerificationCode(email);
        if(verificationCodeJpe == null) return false;
        if(! code.equals(verificationCodeJpe.getCode())) return false;
        if(verificationCodeJpe.getExpiredAt().isBefore(LocalDateTime.now())) return false;

        return true;
    }
}
