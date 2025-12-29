package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.LoginRequest;
import com.hoctap.learningsupportapi.model.dto.LoginResponse;
import com.hoctap.learningsupportapi.model.dto.RegisterRequest;
import com.hoctap.learningsupportapi.model.dto.UserResponse;
import com.hoctap.learningsupportapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}
