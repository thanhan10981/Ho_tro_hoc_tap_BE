package com.hoctap.learningsupportapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoctap.learningsupportapi.model.dto.CreateFlashcardAiRequest;
import com.hoctap.learningsupportapi.model.dto.CreateFlashcardRequest;
import com.hoctap.learningsupportapi.model.entity.BoFlashcard;
import com.hoctap.learningsupportapi.model.entity.Flashcard;
import com.hoctap.learningsupportapi.repository.BoFlashcardRepository;
import com.hoctap.learningsupportapi.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository repository;
    private final BoFlashcardRepository boFlashcardRepository;
    private final GeminiService geminiService;

    /* ======================
       CREATE MANUAL FLASHCARD
    ====================== */
    public Flashcard createFlashcard(
            Integer userId,
            CreateFlashcardRequest req
    ) {
        BoFlashcard bo = boFlashcardRepository
                .findById(req.getMaBoFlashcard())
                .orElseThrow(() -> new RuntimeException("Bộ flashcard không tồn tại"));

        if (!bo.getMaNguoiDung().equals(userId)) {
            throw new RuntimeException("Không có quyền");
        }

        Flashcard card = new Flashcard();
        card.setMaBoFlashcard(req.getMaBoFlashcard());
        card.setMaMonHoc(bo.getMaMonHoc());
        card.setMaNguoiDung(userId);
        card.setMatTruoc(req.getMatTruoc());
        card.setMatSau(req.getMatSau());

        return repository.save(card);
    }

    /* ======================
       GET FLASHCARD BY SET
    ====================== */
    public List<Flashcard> getByBo(Integer maBoFlashcard) {
        return repository.findByMaBoFlashcard(maBoFlashcard);
    }

    /* ======================
       CREATE FLASHCARD BY AI
    ====================== */
    public List<Flashcard> createFlashcardsByAi(
            Integer userId,
            CreateFlashcardAiRequest req
    ) {
        BoFlashcard bo = boFlashcardRepository
                .findById(req.getMaBoFlashcard())
                .orElseThrow(() -> new RuntimeException("Bộ flashcard không tồn tại"));

        if (!bo.getMaNguoiDung().equals(userId)) {
            throw new RuntimeException("Không có quyền");
        }

        // 🔥 LẤY FLASHCARD HIỆN CÓ
        List<Flashcard> existingCards =
                repository.findByMaBoFlashcard(bo.getMaBoFlashcard());

        String existingFronts = existingCards.isEmpty()
                ? "Không có"
                : existingCards.stream()
                .map(Flashcard::getMatTruoc)
                .collect(Collectors.joining(" | "));

        // 🔥 BUILD PROMPT KHÔNG TRÙNG
        String prompt = buildPrompt(
                req.getContent(),
                req.getAmount(),
                existingFronts
        );

        String aiReply = geminiService.askGemini(prompt);

        return parseAndSave(aiReply, bo, userId);
    }

    /* ======================
       PARSE AI RESPONSE & SAVE
    ====================== */
    private List<Flashcard> parseAndSave(
            String aiReply,
            BoFlashcard bo,
            Integer userId
    ) {
        try {
            // 1. CẮT JSON THUẦN
            int start = aiReply.indexOf("{");
            int end = aiReply.lastIndexOf("}");

            if (start == -1 || end == -1) {
                throw new RuntimeException("AI không trả JSON");
            }

            String json = aiReply.substring(start, end + 1);

            // 2. PARSE JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode list = root.get("flashcards");

            if (list == null || !list.isArray()) {
                throw new RuntimeException("JSON không đúng format flashcards");
            }

            List<Flashcard> result = new ArrayList<>();

            for (JsonNode item : list) {
                Flashcard card = new Flashcard();
                card.setMaBoFlashcard(bo.getMaBoFlashcard());
                card.setMaMonHoc(bo.getMaMonHoc());
                card.setMaNguoiDung(userId);
                card.setMatTruoc(item.get("front").asText());
                card.setMatSau(item.get("back").asText());

                result.add(repository.save(card));
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AI trả dữ liệu không hợp lệ");
        }
    }

    /* ======================
       BUILD PROMPT (ANTI DUPLICATE)
    ====================== */
    private String buildPrompt(
            String content,
            int amount,
            String existingFronts
    ) {
        return """
                Bạn là một hệ thống tạo flashcard cho ứng dụng học tập.
                
                NHIỆM VỤ:
                - Tạo %d flashcard MỚI từ nội dung bên dưới
                - TUYỆT ĐỐI KHÔNG tạo flashcard trùng hoặc gần nghĩa
                - Nếu kiến thức cơ bản đã có, hãy tạo flashcard nâng cao hoặc ứng dụng
                
                FLASHCARD ĐÃ TỒN TẠI (KHÔNG ĐƯỢC TRÙNG):
                %s
                
                NỘI DUNG HỌC:
                "%s"
                
                YÊU CẦU ĐỊNH DẠNG:
                - CHỈ trả về JSON
                - KHÔNG markdown
                - KHÔNG giải thích
                - KHÔNG text thừa
                
                FORMAT BẮT BUỘC:
                {
                  "flashcards": [
                    { "front": "...", "back": "..." }
                  ]
                }
                """
                .formatted(amount, existingFronts, content);
    }
}
