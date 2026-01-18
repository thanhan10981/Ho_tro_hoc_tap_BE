package com.hoctap.learningsupportapi.controller;

import java.util.UUID;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import com.hoctap.learningsupportapi.model.dto.SaveNhanRequest;
import com.hoctap.learningsupportapi.model.entity.*;
import com.hoctap.learningsupportapi.repository.*;
import com.hoctap.learningsupportapi.service.KnowledgeService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-docs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PersonalDocController {

    private final TaiLieuNhanRepository taiLieuNhanRepo;
    private final TaiLieuChungRepository taiLieuChungRepo;
    private final GhiChuTaiLieuRepository ghiChuRepo;
    private final KnowledgeService knowledgeService;
    private final NguoiDungRepository nguoiDungRepository;

    /* ================= PDF ================= */

    @GetMapping("/{docId}/pdf")
    public ResponseEntity<Resource> streamPdf(@PathVariable Integer docId)
            throws IOException {

        TaiLieuChung tl = taiLieuChungRepo.findById(docId).orElseThrow();
        Path path = Paths.get(tl.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    /* ================= CANVAS ================= */

    @GetMapping("/{docId}/canvas")
    public GhiChuTaiLieu loadCanvas(
            @PathVariable Integer docId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Integer userId = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getId();

        return ghiChuRepo
                .findTopByTaiLieu_IdAndNguoiDung_IdOrderByCreatedAtDesc(
                        docId, userId
                );
    }

    @PostMapping("/{docId}/canvas")
    public GhiChuTaiLieu saveCanvas(
            @PathVariable Integer docId,
            Authentication authentication,
            @RequestBody String canvasJson
    ) {
        String email = authentication.getName();
        NguoiDung user = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        GhiChuTaiLieu note = new GhiChuTaiLieu();
        note.setTaiLieu(taiLieuChungRepo.getReferenceById(docId));
        note.setNguoiDung(user);
        note.setCanvasJson(canvasJson);
        note.setCreatedAt(LocalDateTime.now());

        return ghiChuRepo.save(note);
    }

    /* ================= NOTES ================= */

    @GetMapping("/{docId}/notes")
    public List<GhiChuTaiLieu> getNotes(
            @PathVariable Integer docId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Integer userId = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getId();

        return ghiChuRepo.findByTaiLieu_IdAndNguoiDung_Id(docId, userId);
    }

    @PostMapping("/{docId}/notes")
    public GhiChuTaiLieu addNote(
            @PathVariable Integer docId,
            Authentication authentication,
            @RequestBody String content
    ) {
        String email = authentication.getName();
        NguoiDung user = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        GhiChuTaiLieu note = new GhiChuTaiLieu();
        note.setTaiLieu(taiLieuChungRepo.getReferenceById(docId));
        note.setNguoiDung(user);
        note.setNoiDung(content);
        note.setCreatedAt(LocalDateTime.now());

        return ghiChuRepo.save(note);
    }

    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable UUID id) {
        ghiChuRepo.deleteById(id);
    }

    /* ================= STATUS ================= */

    @PutMapping("/status")
    public void updateStatus(
            @RequestParam Integer maTaiLieu,
            @RequestParam Integer maNguoiDung,
            @RequestParam Integer maNhan,
            @RequestParam String status
    ) {
        TaiLieuNhanId id = new TaiLieuNhanId(
                maTaiLieu,
                maNguoiDung,
                maNhan
        );

        TaiLieuNhan tln = taiLieuNhanRepo.findById(id).orElseThrow();
        tln.setStatus(status);
        taiLieuNhanRepo.save(tln);
    }

    /* ================== KHO CÁ NHÂN ================== */

    @GetMapping
    public List<PersonalDocResponse> getPersonalDocs(Authentication authentication) {
        String email = authentication.getName();
        NguoiDung user = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        return knowledgeService.getPersonalDocs(user.getId());
    }

    @DeleteMapping
    public ResponseEntity<?> removeFromPersonal(
            Authentication authentication,
            @RequestParam Integer docId
    ) {
        String email = authentication.getName();
        Integer userId = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getId();

        knowledgeService.removeFromPersonal(userId, docId);
        return ResponseEntity.ok().build();
    }

    /* ================= LABEL ================= */

    @PostMapping("/label")
    public ResponseEntity<?> addNhanToDoc(
            Authentication authentication,
            @RequestBody SaveNhanRequest req
    ) {
        String email = authentication.getName();
        Integer userId = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getId();

        knowledgeService.addNhanToPersonalDoc(
                userId,
                req.getDocId(),
                req.getNhanId()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-label")
    public List<PersonalDocResponse> getByNhan(
            Authentication authentication,
            @RequestParam Integer nhanId
    ) {
        return knowledgeService.getDocsByNhan(nhanId);
    }
}
