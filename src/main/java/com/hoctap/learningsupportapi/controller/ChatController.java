package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.AIResponseDTO;
import com.hoctap.learningsupportapi.model.entity.CuocTroChuyenAI;
import com.hoctap.learningsupportapi.model.entity.NguoiDung;
import com.hoctap.learningsupportapi.model.entity.TinNhanAI;
import com.hoctap.learningsupportapi.repository.CuocTroChuyenRepository;
import com.hoctap.learningsupportapi.repository.NguoiDungRepository;
import com.hoctap.learningsupportapi.repository.TinNhanRepository;
import com.hoctap.learningsupportapi.service.ChatService;
import com.hoctap.learningsupportapi.service.CurrentUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:5173",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE }
)
public class ChatController {

    private final CuocTroChuyenRepository conversationRepo;
    private final TinNhanRepository tinNhanRepo;
    private final ChatService chatService;
    private final NguoiDungRepository nguoiDungRepo;
    private final CurrentUserService currentUserService;

    // Tạo cuộc chat
    @PostMapping("/conversation")
    public CuocTroChuyenAI createConversation() {

        Integer userId = currentUserService.getCurrentUserId();
        NguoiDung user = nguoiDungRepo.findById(userId)
                .orElseThrow();

        CuocTroChuyenAI c = new CuocTroChuyenAI();
        c.setUserId(user.getId());
        c.setMonHoc(null);
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

    @GetMapping("/conversation/user")
    public List<CuocTroChuyenAI> getConversations() {

        Integer userId = currentUserService.getCurrentUserId();

        return conversationRepo
                .findByUserIdOrderByCreatedAtDesc(userId);
    }

    @DeleteMapping("/conversation/{id}")
    @Transactional
    public void deleteConversation(@PathVariable Integer id) {
        tinNhanRepo.deleteByMaCuocTroChuyen(id);
        conversationRepo.deleteById(id);
    }
    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("conversationId") Integer conversationId,
            @RequestParam("question") String question
    ) {
        try {
            System.out.println("UPLOAD HIT");

            // 1️⃣ OCR (CHỈ DÙNG NỘI BỘ)
            String extractedText = chatService.extractTextFromFile(file);

            // 2️⃣ LƯU MESSAGE USER (KHÔNG CHỨA OCR)
            chatService.saveUserMessage(
                    conversationId,
                    "Đã tải lên file: " + file.getOriginalFilename()
                            + "\nCâu hỏi: " + question
            );

            // 3️⃣ PROMPT CHỈ DÙNG CHO AI
            String aiPrompt = """
            Bạn là AI trợ lý học tập.
            
            Dưới đây là nội dung tài liệu (KHÔNG được nhắc lại toàn bộ):
            ------------------------
            %s
            
            Yêu cầu của người dùng:
            %s
            """.formatted(extractedText, question);

            // 4️⃣ GỌI AI + LƯU AI MESSAGE
            return chatService.askAndSaveAI(conversationId, aiPrompt);

        } catch (Exception e) {
            e.printStackTrace();
            return "Không đọc được nội dung file";
        }
    }



}
