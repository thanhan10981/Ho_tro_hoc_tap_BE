package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.entity.CuocTroChuyenAI;
import com.hoctap.learningsupportapi.model.entity.TinNhanAI;
import com.hoctap.learningsupportapi.repository.CuocTroChuyenRepository;
import com.hoctap.learningsupportapi.repository.TinNhanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CuocTroChuyenRepository conversationRepo;
    private final TinNhanRepository tinNhanRepo;
    private final GeminiService geminiService;

    public String sendMessage(Integer conversationId, String message) {

        CuocTroChuyenAI conversation =
                conversationRepo.findById(conversationId)
                        .orElseThrow();

        // 1️⃣ Lưu tin nhắn người dùng
        TinNhanAI userMsg = new TinNhanAI();
        userMsg.setConversation(conversation);
        userMsg.setSender("nguoi_dung");
        userMsg.setContent(message);
        userMsg.setCreatedAt(LocalDateTime.now());
        tinNhanRepo.save(userMsg);

        // 2️⃣ Gọi AI
        String aiReply = geminiService.askGemini(message);

        // 3️⃣ Lưu tin nhắn AI
        TinNhanAI aiMsg = new TinNhanAI();
        aiMsg.setConversation(conversation);
        aiMsg.setSender("ai");
        aiMsg.setContent(aiReply);
        aiMsg.setCreatedAt(LocalDateTime.now());
        tinNhanRepo.save(aiMsg);

        return aiReply;
    }
}
