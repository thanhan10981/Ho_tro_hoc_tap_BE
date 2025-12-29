package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.UserProfileResponse;
import com.hoctap.learningsupportapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponse getMyProfile() {
        return userService.getCurrentUserProfile();
    }
}
