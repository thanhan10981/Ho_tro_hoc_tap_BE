package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.entity.CuocTroChuyenAI;
import com.hoctap.learningsupportapi.model.entity.TinNhanAI;
import com.hoctap.learningsupportapi.repository.CuocTroChuyenRepository;
import com.hoctap.learningsupportapi.repository.TinNhanRepository;
import com.hoctap.learningsupportapi.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final CuocTroChuyenRepository conversationRepo;
    private final TinNhanRepository tinNhanRepo;
    private final ChatService chatService;

    // 1️⃣ Tạo cuộc chat
    @PostMapping("/conversation")
    public CuocTroChuyenAI createConversation(@RequestBody Map<String, Integer> body) {
        CuocTroChuyenAI c = new CuocTroChuyenAI();
        c.setUserId(body.get("userId"));
        c.setMonHocId(body.get("monHocId"));
        c.setCreatedAt(LocalDateTime.now());
        return conversationRepo.save(c);
    }

    // 2️⃣ Load lịch sử chat
    @GetMapping("/conversation/{id}")
    public List<TinNhanAI> getMessages(@PathVariable Integer id) {
        return tinNhanRepo.findByConversation_IdOrderByCreatedAtAsc(id);
    }

    // 3️⃣ Gửi message
    @PostMapping("/message")
    public String sendMessage(@RequestBody Map<String, String> body) {
        return chatService.sendMessage(
                Integer.valueOf(body.get("conversationId")),
                body.get("message")
        );
    }
}
