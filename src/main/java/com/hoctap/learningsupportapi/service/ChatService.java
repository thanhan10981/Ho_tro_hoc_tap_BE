package com.hoctap.learningsupportapi.service;
import com.hoctap.learningsupportapi.model.dto.AIResponseDTO;
import com.hoctap.learningsupportapi.model.entity.CuocTroChuyenAI;
import com.hoctap.learningsupportapi.model.entity.TinNhanAI;
import com.hoctap.learningsupportapi.repository.CuocTroChuyenRepository;
import com.hoctap.learningsupportapi.repository.TinNhanRepository;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import org.apache.pdfbox.rendering.PDFRenderer;
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

        // Gọi AI
        String aiReply = geminiService.askGemini(message);

        // Lưu tin nhắn AI
        TinNhanAI aiMsg = new TinNhanAI();
        aiMsg.setConversation(conversation);
        aiMsg.setSender("ai");
        aiMsg.setContent(aiReply);
        aiMsg.setCreatedAt(LocalDateTime.now());
        tinNhanRepo.save(aiMsg);

        return aiReply;
    }
    public String askAndSaveAI(Integer conversationId, String aiPrompt) {
        CuocTroChuyenAI conversation =
                conversationRepo.findById(conversationId)
                        .orElseThrow(() -> new RuntimeException("Conversation not found"));

        String aiReply;

        try {
            aiReply = geminiService.askGemini(aiPrompt);
        } catch (Exception e) {
            aiReply = "AI hiện không phản hồi được, vui lòng thử lại sau.";
        }

        TinNhanAI aiMsg = new TinNhanAI();
        aiMsg.setConversation(conversation);
        aiMsg.setSender("ai");
        aiMsg.setContent(aiReply);
        aiMsg.setCreatedAt(LocalDateTime.now());

        tinNhanRepo.save(aiMsg);

        return aiReply;
    }


    public String extractTextFromFile(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename().toLowerCase();

        if (filename.endsWith(".pdf")) {
            return extractPdf(file);
        }

        if (filename.endsWith(".docx")) {
            return extractDocx(file);
        }

        if (filename.endsWith(".doc")) {
            throw new RuntimeException("Không hỗ trợ file .doc (Word 2003). Vui lòng lưu lại dưới dạng .docx");
        }

        if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return extractImage(file);
        }

        throw new RuntimeException("File không được hỗ trợ");
    }
    private String extractDocx(MultipartFile file) throws Exception {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {

            StringBuilder sb = new StringBuilder();

            // Paragraphs
            document.getParagraphs().forEach(p -> sb.append(p.getText()).append("\n"));

            // Tables
            document.getTables().forEach(table ->
                    table.getRows().forEach(row ->
                            row.getTableCells().forEach(cell ->
                                    sb.append(cell.getText()).append("\n")
                            )
                    )
            );

            String text = sb.toString().trim();

            if (text.isEmpty()) {
                throw new RuntimeException("File Word không có text (có thể là scan)");
            }

            return text;
        }
    }


    private String extractPdf(MultipartFile file) throws Exception {
        try (PDDocument doc =
                     Loader.loadPDF(new RandomAccessReadBuffer(file.getInputStream()))) {

            if (doc.isEncrypted()) {
                throw new RuntimeException("PDF bị mã hóa");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc).trim();

            if (!text.isEmpty()) {
                return text;
            }

            // PDF scan → OCR
            return ocrPdf(file);
        }
    }
    private String ocrPdf(MultipartFile file) throws Exception {

        try (PDDocument document =
                     Loader.loadPDF(new RandomAccessReadBuffer(file.getInputStream()))) {

            PDFRenderer renderer = new PDFRenderer(document);

            Tesseract tesseract = new Tesseract();
             tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

            tesseract.setLanguage("vie+eng");
            tesseract.setPageSegMode(1);
            tesseract.setOcrEngineMode(1);

            StringBuilder result = new StringBuilder();

            int pageCount = document.getNumberOfPages();

            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(
                        i, 300, ImageType.RGB);

                String pageText = tesseract.doOCR(image);
                result.append(pageText).append("\n");
            }

            String text = result.toString().trim();

            if (text.isEmpty()) {
                throw new RuntimeException("OCR không đọc được nội dung PDF");
            }

            return text;
        }
    }

    private String extractImage(MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new RuntimeException("Không xác định được định dạng ảnh");
        }

        String ext = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        if (!ext.equals(".png") && !ext.equals(".jpg") && !ext.equals(".jpeg")) {
            throw new RuntimeException("Chỉ hỗ trợ ảnh PNG, JPG, JPEG");
        }

        File temp = File.createTempFile("ocr_", ext);
        file.transferTo(temp);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng+vie");

        String text = tesseract.doOCR(temp);

        temp.delete();

        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Không nhận dạng được chữ trong ảnh");
        }

        return text;
    }
    public void saveUserMessage(Integer conversationId, String content) {
        CuocTroChuyenAI conversation =
                conversationRepo.findById(conversationId).orElseThrow();

        TinNhanAI msg = new TinNhanAI();
        msg.setConversation(conversation);
        msg.setSender("nguoi_dung");
        msg.setContent(content);
        msg.setCreatedAt(LocalDateTime.now());

        tinNhanRepo.save(msg);
    }




}
