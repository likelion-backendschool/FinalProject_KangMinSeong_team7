package com.example.ebook.global.util.mail;

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

    public void sendUsername(String toEmail, String username) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("eBook 홈페이지 아이디 정보입니다.");
        simpleMailMessage.setText(toEmail + "님의 아이디는\n" + username + "\n입니다.");

        javaMailSender.send(simpleMailMessage);
    }

    public void sendTemporaryPassword(String toEmail, String password) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("eBook 홈페이지 임시 비밀번호 정보입니다.");
        simpleMailMessage.setText(toEmail + "님의 임시 비밀번호는\n" + password + "\n입니다.");

        javaMailSender.send(simpleMailMessage);
    }
}
