package com.spring.auth.email.service;


import com.spring.auth.email.MailContent;

public interface EmailService {
    void sendEmail(String email);
    boolean verifyCode(String email, String code);
    MailContent generateMessage(String email);
}
