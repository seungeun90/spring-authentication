package com.spring.auth.aggregate.email;

import com.spring.auth.aggregate.email.jpe.VerificationCodeJpe;
import com.spring.auth.aggregate.email.repository.RedisVerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class VerificationCodeRedisStore implements VerificationCodeStore {

    private final RedisVerificationCodeRepository codeRepository;

    @Override
    public void saveVerificationCode(VerificationCodeJpe jpe) {
        codeRepository.save(jpe);
    }

    @Override
    public VerificationCodeJpe retrieveVerificationCode(String email) {
        Optional<VerificationCodeJpe> byId = codeRepository.findById(email);
        return byId.isEmpty() ? null : byId.get();
    }
}
