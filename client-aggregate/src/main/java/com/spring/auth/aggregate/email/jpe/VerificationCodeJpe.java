package com.spring.auth.aggregate.email.jpe;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value="code")
public class VerificationCodeJpe {

    @Id
    private String email;
    private String code;
    private LocalDateTime expiredAt;

}
