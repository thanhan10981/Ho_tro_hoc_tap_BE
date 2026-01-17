package com.hoctap.learningsupportapi.service;

import com.google.genai.Client;
import com.google.genai.errors.ApiException;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final Client client;

    public String askGemini(String prompt) {
        try {
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            prompt,
                            null
                    );

            return response.text();

        } catch (ApiException e) {
            if (e.getMessage() != null && e.getMessage().contains("503")) {
                return "AI đang quá tải, vui lòng thử lại sau.";
            }
            return "Không thể xử lý yêu cầu AI lúc này.";

        } catch (Exception e) {

            return "Đã xảy ra lỗi khi gọi AI.";
        }
    }
}
