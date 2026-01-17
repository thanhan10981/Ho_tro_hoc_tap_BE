package com.hoctap.learningsupportapi.utils.summary;

public class TomTatTitlePromptBuilder {

    public static String build(String noiDung) {
        return """
        Hãy tạo một tiêu đề ngắn gọn (tối đa 15 từ),
        phản ánh đúng nội dung tóm tắt sau:

        %s

        Chỉ trả về tiêu đề, không giải thích.
        """.formatted(noiDung);
    }
}
