package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.AIItemDTO;
import com.hoctap.learningsupportapi.model.dto.AIResponseDTO;
import com.hoctap.learningsupportapi.model.entity.LichHoc;
import com.hoctap.learningsupportapi.model.enums.LoaiSuKien;
import org.springframework.stereotype.Component;


import java.util.List;
@Component
public class ResponseFormatter {


    public AIResponseDTO eventWeek(
            Integer userId,
            LoaiSuKien loai,
            List<LichHoc> events
    ) {
        String title = (loai == null)
                ? "📅 Các sự kiện tuần này:"
                : switch (loai) {
            case EXAM -> "📝 Bài thi:";
            case DEADLINE -> "⏰ Deadline:";
            case CLASS -> "🏫 Lịch học:";
            case STUDY -> "📖 Ôn tập:";
        };


        return new AIResponseDTO(
                "👋 Chào An!",
                title + " (" + events.size() + " sự kiện)",
                events.stream().map(e ->
                        new AIItemDTO(
                                e.getMaSuKien(),
                                e.getTieuDe(),
                                e.getThoiGianBatDau().toLocalDate().toString(),
                                e.getThoiGianBatDau().toLocalTime().toString(),
                                e.getDiaDiem()
                        )
                ).toList(),
                List.of(
                        "📅 Nhắc lịch trước sự kiện",
                        "🧠 Lên kế hoạch ôn tập",
                        "🔥 Tạo quiz luyện nhanh"
                )
        );
    }

    public AIResponseDTO noData(Integer userId) {
        return new AIResponseDTO(
                "📭 Chào An!",
                "Tuần này bạn chưa có sự kiện quan trọng nào.",
                List.of(),
                List.of(
                        "📖 Ôn lại môn yếu",
                        "📝 Tạo quiz tự luyện",
                        "📅 Xem lịch học"
                )
        );
    }
}
