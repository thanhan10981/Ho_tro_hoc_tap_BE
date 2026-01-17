package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.AIResponseDTO;
import com.hoctap.learningsupportapi.model.entity.LichHoc;
import com.hoctap.learningsupportapi.model.enums.LoaiSuKien;
import com.hoctap.learningsupportapi.repository.LichHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AssistantService {

    private final CurrentUserService currentUserService;
    private final IntentService intentService;
    private final LichHocRepository lichHocRepo;
    private final ResponseFormatter formatter;

    public AIResponseDTO handleAssistant(String message) {

        Integer userId = currentUserService.getCurrentUserId();
        String intent = intentService.detectIntent(message);

        if ("EXAM_WEEK".equals(intent)) {
            return handleEventWeek(userId, LoaiSuKien.EXAM);
        }

        if ("DEADLINE_WEEK".equals(intent)) {
            return handleEventWeek(userId, LoaiSuKien.DEADLINE);
        }

        if ("CLASS_WEEK".equals(intent)) {
            return handleEventWeek(userId, LoaiSuKien.CLASS);
        }

        if ("UPCOMING_EVENTS".equals(intent)) {
            return handleAllEventWeek(userId); // ✅ QUAN TRỌNG
        }

        return formatter.noData(userId);
    }

    private AIResponseDTO handleAllEventWeek(Integer userId) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(7);

        List<LichHoc> events =
                lichHocRepo.findAllTrongTuan(userId, now, end);

        if (events.isEmpty()) {
            return formatter.noData(userId);
        }

        return formatter.eventWeek(userId, null, events); // loai = null
    }

    private AIResponseDTO handleEventWeek(
            Integer userId,
            LoaiSuKien loai
    ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(7);

        List<LichHoc> events = lichHocRepo
                .findSuKienTrongTuan(userId, loai, now, end);

        if (events.isEmpty()) {
            return formatter.noData(userId);
        }

        return formatter.eventWeek(userId, loai, events);
    }
}
