package com.Aga.Agali.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Təsdiqləmə");
        message.setText("Emailinizi təsdiqləmək üçün aşağıdakı linkə keçin:\n\n"
                + "https://unpermeable-according-shae.ngrok-free.dev/api/auth/verify?token=" + token);
        mailSender.send(message);
    }
    public void sendResetPasswordEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Şifrə Sıfırlama");
        message.setText("Şifrənizi sıfırlamaq üçün aşağıdakı linkə keçin:\n\n"
                + "https://unpermeable-according-shae.ngrok-free.dev/api/auth/reset-password?token=" + token
                + "\n\nBu link 30 dəqiqə ərzində etibarlıdır.");
        mailSender.send(message);
    }
}