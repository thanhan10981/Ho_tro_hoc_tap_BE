package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.QuizQuestionResponse;
import com.hoctap.learningsupportapi.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class QuizQuestionController {

    private final QuizQuestionService quizQuestionService;

    @GetMapping("/{maQuiz}/questions")
    public List<QuizQuestionResponse> getQuizQuestions(
            @PathVariable Integer maQuiz
    ) {
        return quizQuestionService.getQuestionsByQuiz(maQuiz);
    }
}
