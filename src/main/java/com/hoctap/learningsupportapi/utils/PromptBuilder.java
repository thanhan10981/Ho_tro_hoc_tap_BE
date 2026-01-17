package com.hoctap.learningsupportapi.utils;

public class PromptBuilder {

    public static String build(
            String topic,
            Integer numQuestions,
            String difficulty
    ) {
        return """
        Bạn là hệ thống tạo câu hỏi trắc nghiệm cho ứng dụng học tập.

        Chủ đề: %s
        Số lượng câu hỏi: %d
        Độ khó: %s

        QUY TẮC BẮT BUỘC:
        - Chỉ trả về JSON hợp lệ
        - KHÔNG markdown
        - KHÔNG giải thích
        - KHÔNG chữ thừa
        - KHÔNG ```json
        - Mỗi câu hỏi có đúng 4 đáp án
        - Chỉ 1 đáp án đúng
        - Dùng tiếng Việt

        FORMAT JSON CHÍNH XÁC (không thêm gì khác):

        {
          "questions": [
            {
              "noiDung": "string",
              "dapAn": [
                { "noiDung": "string", "isDung": true }
              ]
            }
          ]
        }
        """.formatted(topic, numQuestions, difficulty);
    }
}
