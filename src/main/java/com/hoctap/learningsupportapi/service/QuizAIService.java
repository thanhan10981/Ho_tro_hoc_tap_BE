package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.AIAnswer;
import com.hoctap.learningsupportapi.model.dto.AIQuestion;
import com.hoctap.learningsupportapi.model.dto.AIResponse;
import com.hoctap.learningsupportapi.model.entity.CauHoiQuiz;
import com.hoctap.learningsupportapi.model.entity.DapAnQuiz;
import com.hoctap.learningsupportapi.repository.CauHoiQuizRepository;
import com.hoctap.learningsupportapi.repository.DapAnQuizRepository;
import com.hoctap.learningsupportapi.utils.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizAIService {

    private final GeminiService geminiService;
    private final CauHoiQuizRepository cauHoiRepo;
    private final DapAnQuizRepository dapAnRepo;
    private final QuizAIJsonParser parser;

    public void generateAndSaveQuestions(
            Integer maQuiz,
            String topic,
            Integer numQuestions,
            String difficulty
    ) {
        String prompt = PromptBuilder.build(topic, numQuestions, difficulty);

        String aiRawText = geminiService.askGemini(prompt);

        // 🔥 QUAN TRỌNG: CLEAN JSON
        String json = extractJson(aiRawText);

        AIResponse parsed = parser.parse(json);

        for (AIQuestion q : parsed.getQuestions()) {
            CauHoiQuiz cauHoi = new CauHoiQuiz();
            cauHoi.setMaQuiz(maQuiz);
            cauHoi.setNoiDung(q.getNoiDung());
            cauHoiRepo.save(cauHoi);

            for (AIAnswer a : q.getDapAn()) {
                DapAnQuiz dapAn = new DapAnQuiz();
                dapAn.setCauHoi(cauHoi);
                dapAn.setNoiDung(a.getNoiDung());
                dapAn.setIsDung(a.getIsDung());
                dapAnRepo.save(dapAn);
            }
        }
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start == -1 || end == -1) {
            throw new RuntimeException("AI không trả JSON hợp lệ");
        }

        return text.substring(start, end + 1);
    }
}
