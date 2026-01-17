package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.AIResponseDTO;
import com.hoctap.learningsupportapi.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AssistantController {

    private final AssistantService assistantService;

    @PostMapping("/ask")
    public AIResponseDTO ask(@RequestBody Map<String, String> body) {
        return assistantService.handleAssistant(
                body.get("message")
        );
    }

}
