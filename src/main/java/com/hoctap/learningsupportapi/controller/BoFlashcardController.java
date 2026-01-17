package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.CreateBoFlashcardRequest;
import com.hoctap.learningsupportapi.model.entity.BoFlashcard;
import com.hoctap.learningsupportapi.service.BoFlashcardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.hoctap.learningsupportapi.model.dto.FlashcardSetResponse;
import java.util.List;

@RestController
@RequestMapping("/api/flashcard-sets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BoFlashcardController {

    private final BoFlashcardService boFlashcardService;

    @PostMapping
    public BoFlashcard createBoFlashcard(
            @RequestBody CreateBoFlashcardRequest request,
            HttpServletRequest httpRequest
    ) {
        Integer userId =
                (Integer) httpRequest.getAttribute("currentUserId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return boFlashcardService.createBoFlashcard(
                userId,
                request.getMaMonHoc(),
                request.getTenBo(),
                request.getMoTa()
        );
    }

    @GetMapping
    public List<FlashcardSetResponse> getMyFlashcardSets(
            HttpServletRequest httpRequest
    ) {
        Integer userId = (Integer) httpRequest.getAttribute("currentUserId");

        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        return boFlashcardService.getMyFlashcardSets(userId);
    }

}
