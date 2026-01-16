package com.hoctap.learningsupportapi.utils.summary;


import com.hoctap.learningsupportapi.model.dto.summary.TomTatPreviewRequest;

public class TomTatPromptBuilder {

    public static String build(TomTatPreviewRequest req) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("Bạn là trợ lý học tập. Hãy tóm tắt nội dung sau bằng tiếng Việt.\n");

        switch (req.getDoDai()) {
            case "NGAN" -> prompt.append("Yêu cầu: Ngắn gọn, ý chính, gạch đầu dòng.\n");
            case "VUA" -> prompt.append("Yêu cầu: Cân bằng, dễ học.\n");
            case "DAI" -> prompt.append("Yêu cầu: Chi tiết, chia mục rõ ràng.\n");
        }

        if (req.isHighlightTuKhoa())
            prompt.append("- Highlight từ khóa quan trọng.\n");

        if (req.isThemViDu())
            prompt.append("- Thêm ví dụ minh họa.\n");

        if (req.isTaoCauHoiOnTap())
            prompt.append("- Cuối bài tạo 3–5 câu hỏi ôn tập.\n");

        prompt.append("\nNội dung cần tóm tắt:\n");
        prompt.append(req.getNoiDung());

        return prompt.toString();
    }
}
