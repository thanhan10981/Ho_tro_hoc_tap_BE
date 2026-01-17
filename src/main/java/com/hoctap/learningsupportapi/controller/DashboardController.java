package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.UpcomingEventDto;

import com.hoctap.learningsupportapi.service.CurrentUserService;
import com.hoctap.learningsupportapi.service.LichHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class DashboardController {

    private final LichHocService lichHocService;
    private final CurrentUserService currentUserService;

    @GetMapping("/lich-hoc/upcoming")
    public List<UpcomingEventDto> getUpcomingEvents() {
        Integer userId = currentUserService.getCurrentUserId();
        return lichHocService.getUpcomingEvents(userId);
    }
}

