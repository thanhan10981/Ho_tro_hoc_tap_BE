package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.CreateFlashcardAiRequest;
import com.hoctap.learningsupportapi.model.dto.CreateFlashcardRequest;
import com.hoctap.learningsupportapi.model.entity.Flashcard;
import com.hoctap.learningsupportapi.service.FlashcardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FlashcardController {

    private final FlashcardService service;

    @PostMapping("/manual")
    public Flashcard createManual(
            @RequestBody CreateFlashcardRequest request,
            HttpServletRequest httpRequest
    ) {
        Integer userId =
                (Integer) httpRequest.getAttribute("currentUserId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return service.createFlashcard(userId, request);
    }

    @GetMapping("/by-set/{maBoFlashcard}")
    public List<Flashcard> getBySet(
            @PathVariable Integer maBoFlashcard
    ) {
        return service.getByBo(maBoFlashcard);
    }

    @PostMapping("/ai")
    public List<Flashcard> createByAi(
            @RequestBody CreateFlashcardAiRequest request,
            HttpServletRequest httpRequest
    ) {
        Integer userId = (Integer) httpRequest.getAttribute("currentUserId");
        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return service.createFlashcardsByAi(userId, request);
    }

}
