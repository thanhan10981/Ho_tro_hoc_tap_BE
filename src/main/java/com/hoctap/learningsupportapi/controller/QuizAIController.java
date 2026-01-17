package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.AIQuizGenerateRequest;
import com.hoctap.learningsupportapi.service.QuizAIService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quiz-ai")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class QuizAIController {

    private final QuizAIService quizAIService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateQuizByAI(
            @RequestBody AIQuizGenerateRequest request,
            HttpServletRequest httpRequest
    ) {
        Integer userId = (Integer) httpRequest.getAttribute("currentUserId");
        if (userId == null) throw new RuntimeException("Unauthorized");

        quizAIService.generateAndSaveQuestions(
                request.getMaQuiz(),
                request.getTopic(),
                request.getNumQuestions(),
                request.getDifficulty()
        );

        return ResponseEntity.ok(Map.of(
                "message", "AI đã tạo câu hỏi thành công"
        ));
    }



}
