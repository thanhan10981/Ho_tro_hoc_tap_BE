package com.hoctap.learningsupportapi.controller.lichhoc;

import com.hoctap.learningsupportapi.service.lichhoc.EmailOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email-otp")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EmailOtpController {

    private final EmailOtpService emailOtpService;

    /**
     * Gửi OTP tới email
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Email không được để trống");
        }

        emailOtpService.sendOtp(email);

        return ResponseEntity.ok("OTP đã được gửi tới email");
    }

    /**
     * Xác minh OTP
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest()
                    .body("Thiếu email hoặc otp");
        }

        boolean valid = emailOtpService.verify(email, otp);

        if (!valid) {
            return ResponseEntity.badRequest()
                    .body("OTP không đúng hoặc đã hết hạn");
        }

        return ResponseEntity.ok("Xác minh OTP thành công");
    }
}
