package com.hoctap.learningsupportapi.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final HttpServletRequest request;

    public Integer getCurrentUserId() {
        Object userId = request.getAttribute("currentUserId");
        if (userId == null) {
            throw new RuntimeException("Không xác định được người dùng hiện tại");
        }
        return (Integer) userId;
    }
}
