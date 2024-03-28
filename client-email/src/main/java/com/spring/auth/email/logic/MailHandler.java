package com.spring.auth.email.logic;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Getter
public class MailHandler {
    private JavaMailSender sender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    public MailHandler(JavaMailSender jSender) {
        this.sender = jSender;
        message = jSender.createMimeMessage();
        try {
            messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 보내는 사람 이메일
    public void setFrom(String fromAddress) throws MessagingException {
        messageHelper.setFrom(fromAddress);
    }

    // 받는 사람 이메일
    public void setTo(String email) throws MessagingException {
        messageHelper.setTo(email);
    }

    // 제목
    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    // 메일 내용
    public void setText(String text, boolean useHtml) throws MessagingException {
        messageHelper.setText(text, useHtml);
    }

    // 발송
    public void send() {
        try {
            sender.send(message);
        } catch(Exception e) {
           throw new RuntimeException(e.getMessage());
        }
    }
}
