package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.LoginRequest;
import com.hoctap.learningsupportapi.model.dto.LoginResponse;
import com.hoctap.learningsupportapi.model.dto.RegisterRequest;
import com.hoctap.learningsupportapi.model.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);

}
