package com.spring.auth.aggregate.email.repository;


import com.spring.auth.aggregate.email.jpe.VerificationCodeJpe;
import org.springframework.data.repository.CrudRepository;

public interface RedisVerificationCodeRepository extends CrudRepository<VerificationCodeJpe, String> {
}
