package com.spring.auth.rest;

import com.spring.auth.domain.pass.PassResult;
import com.spring.auth.pass.service.PassService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PassResource {
    private final PassService passService;


    /**
     * 본인인증 표준창 팝업 요청
     * 인증기관 검증
     * */
    @PostMapping("/pass")
    public ResponseEntity<String> pass(HttpServletRequest request) {
        String userToken = passService.getUserToken(request.getSession());
        return ResponseEntity.ok(userToken);
    }

    /**
     * 본인인증 완료 후 개인 정보 수집
     * */
    @PostMapping(value = "/pass/user")
    public ResponseEntity<Account> user(@RequestAttribute PassResult result) {

        return ResponseEntity.ok(passService.getUserInfo(result));
    }



}
