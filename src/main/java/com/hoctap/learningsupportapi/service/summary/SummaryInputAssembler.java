package com.hoctap.learningsupportapi.service.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryInputAssembler {

    private static final int MAX_FILES = 5;
    private static final int MAX_TOTAL_WORDS = 5000;

    private final FileTextExtractorRouter extractorRouter;

    public String assemble(String text, List<MultipartFile> files) {

        if ((text == null || text.isBlank()) &&
                (files == null || files.isEmpty())) {
            throw new RuntimeException("Phải nhập text hoặc đính kèm file");
        }

        StringBuilder content = new StringBuilder();

        // 1️⃣ Text người dùng nhập
        if (text != null && !text.isBlank()) {
            content.append("Nội dung người dùng bổ sung:\n")
                    .append(text)
                    .append("\n\n");
        }

        // 2️⃣ File / ảnh
        if (files != null && !files.isEmpty()) {

            if (files.size() > MAX_FILES) {
                throw new RuntimeException("Chỉ được upload tối đa " + MAX_FILES + " file");
            }

            for (MultipartFile file : files) {
                validateFile(file);

                content.append("Nội dung trích từ file ")
                        .append(file.getOriginalFilename())
                        .append(":\n");

                content.append(extractorRouter.extract(file))
                        .append("\n\n");
            }
        }

        int totalWords = countWords(content.toString());
        if (totalWords > MAX_TOTAL_WORDS) {
            throw new RuntimeException("Tổng nội dung vượt quá 5000 chữ");
        }

        return content.toString();
    }

    private void validateFile(MultipartFile file) {
        long sizeMB = file.getSize() / (1024 * 1024);
        String name = file.getOriginalFilename().toLowerCase();

        if (name.endsWith(".pdf") && sizeMB > 15)
            throw new RuntimeException("PDF tối đa 15MB");

        if ((name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"))
                && sizeMB > 5)
            throw new RuntimeException("Ảnh tối đa 5MB");

        if ((name.endsWith(".docx") || name.endsWith(".pptx"))
                && sizeMB > 10)
            throw new RuntimeException("Office tối đa 10MB");
    }

    private int countWords(String text) {
        return text.trim().split("\\s+").length;
    }
}
