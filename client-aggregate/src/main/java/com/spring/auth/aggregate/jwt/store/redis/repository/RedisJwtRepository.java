package com.spring.auth.aggregate.jwt.store.redis.repository;

import com.spring.auth.aggregate.jwt.store.redis.jpe.TokenJpe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisJwtRepository extends CrudRepository<TokenJpe, String> {
    List<TokenJpe> findByEmail(String email);
}
