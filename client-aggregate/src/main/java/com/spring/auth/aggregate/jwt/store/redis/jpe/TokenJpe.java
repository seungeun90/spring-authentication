package com.spring.auth.aggregate.jwt.store.redis.jpe;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value="token")
public class TokenJpe {

    @Id
    private String deviceId;

    @Indexed
    private String email;

    private String refreshToken;


}
