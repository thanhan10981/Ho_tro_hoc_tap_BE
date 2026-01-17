package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.CreateQuizRequest;
import com.hoctap.learningsupportapi.model.dto.QuizResponse;
import com.hoctap.learningsupportapi.model.entity.BoQuiz;
import com.hoctap.learningsupportapi.service.BoQuizService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BoQuizController {

    private final BoQuizService boQuizService;

    @PostMapping
    public BoQuiz createQuiz(
            @RequestBody CreateQuizRequest request,
            HttpServletRequest httpRequest
    ) {
        Integer userId =
                (Integer) httpRequest.getAttribute("currentUserId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return boQuizService.createQuiz(userId, request);
    }

    @GetMapping
    public List<QuizResponse> getMyQuizzes(
            HttpServletRequest httpRequest
    ) {
        Integer userId =
                (Integer) httpRequest.getAttribute("currentUserId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return boQuizService.getMyQuizzes(userId);
    }
}
