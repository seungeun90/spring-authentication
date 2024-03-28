package com.spring.auth.email;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MailContent {
    private String mailTo;
    private String title;
    private String content;
    private String verificationCode;
    public MailContent(String email, String content, String code) {
        this.mailTo = email;
        this.title = "[헤이홈] 인증번호가 도착했어요.";
        this.content = content;
        this.verificationCode = code;
    }


}
