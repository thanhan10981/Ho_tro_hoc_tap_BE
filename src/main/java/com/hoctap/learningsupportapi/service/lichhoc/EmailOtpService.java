package com.hoctap.learningsupportapi.service.lichhoc;

import org.springframework.beans.factory.annotation.Value;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class EmailOtpService {

    private final OtpStore otpStore;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendOtp(String email) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Email không hợp lệ");
        }

        String otp = String.valueOf(
                100000 + new SecureRandom().nextInt(900000)
        );

        otpStore.save(email, otp, 5);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject("Mã xác minh đổi email");
            helper.setText(
                    "Mã OTP của bạn là: " + otp + "\nCó hiệu lực 5 phút.",
                    false
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }

    public boolean verify(String email, String otp) {
        return otpStore.verify(email, otp);
    }
}
