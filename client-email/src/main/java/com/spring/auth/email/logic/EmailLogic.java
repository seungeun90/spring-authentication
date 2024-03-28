package com.spring.auth.email.logic;

import com.spring.auth.aggregate.email.service.VerificationCodeService;
import com.spring.auth.email.MailContent;
import com.spring.auth.email.config.MailProperties;
import com.spring.auth.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailLogic implements EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties properties;
    private final VerificationCodeService codeService;

    @Override
    public void sendEmail(String email) {
        MailHandler mailHandler = new MailHandler(mailSender);
        try {
            MailContent mailContent = generateMessage(email);
            /** 메일 전송 */
            // 받는 사람
            mailHandler.setTo(email);
            mailHandler.setFrom(properties.getUsername());
            // 제목
            mailHandler.setSubject(mailContent.getTitle());
            // 내용
            mailHandler.setText(mailContent.getContent(), true);
            mailHandler.send();

            /** redis 에 저장 */
            codeService.save(mailContent.getMailTo(), mailContent.getVerificationCode());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyCode(String email, String code) {
        return codeService.validate(email, code);
    }

    @Override
    public MailContent generateMessage(String email) {
        String content = readHtmlFile();
        String code = generateVerificationCode();
        content = content.replace("123456",code);
        if(content==null) content = code;
        return new MailContent(email, content, code);
    }

    private String generateVerificationCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    private String readHtmlFile()  {
        try {
            InputStream inputStream = new ClassPathResource(properties.getImagePath()).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder plainTextBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                plainTextBuilder.append(line).append("\n");
            }

            return plainTextBuilder.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
