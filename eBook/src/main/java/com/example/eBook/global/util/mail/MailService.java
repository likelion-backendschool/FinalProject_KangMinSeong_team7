package com.example.eBook.global.util.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendSingUpMail(String toEmail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("eBook 홈페이지 가입을 축하합니다.");
        simpleMailMessage.setText("eBook 홈페이지 가입을 축하합니다.\n" + "오늘도 좋은 하루 되세요!");

        javaMailSender.send(simpleMailMessage);
    }
}
