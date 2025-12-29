package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.entity.CuocTroChuyenAI;
import com.hoctap.learningsupportapi.model.entity.TinNhanAI;
import com.hoctap.learningsupportapi.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/ask")
    public String askGemini(@RequestBody Map<String, String> body) {
        return geminiService.askGemini(body.get("prompt"));
    }
}
