package com.spring.auth.rest;

import com.spring.auth.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailResource {

    private final EmailService emailService;

    /**
     * 메일 전송
     * */
    @PostMapping("/email")
    public ResponseEntity sendEmail(@RequestBody EmailDto request) {

        emailService.sendEmail(request.getEmail());
        return ResponseData.success(HttpStatus.OK);
    }

    /**
     * 메일 인증 번호 검증
     * */
    @PostMapping("/email/code")
    public ResponseEntity<?> verifyCode(@RequestBody EmailDto request ) {
        return ResponseData.success(emailService.verifyCode(request.getEmail(), request.getCode()));
    }
}
