package com.hoctap.learningsupportapi.controller;
import java.util.UUID;

import com.hoctap.learningsupportapi.model.entity.*;
import com.hoctap.learningsupportapi.repository.GhiChuTaiLieuRepository;
import com.hoctap.learningsupportapi.repository.TaiLieuChungRepository;
import com.hoctap.learningsupportapi.repository.TaiLieuNhanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/personal-docs")
@RequiredArgsConstructor
public class PersonalDocController {

    private final TaiLieuNhanRepository taiLieuNhanRepo;
    private final TaiLieuChungRepository taiLieuChungRepo;
    private final GhiChuTaiLieuRepository ghiChuRepo;


    /* ================= PDF ================= */

    @GetMapping("/{docId}/pdf")
    public ResponseEntity<Resource> streamPdf(@PathVariable Integer  docId)
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
            @PathVariable Integer  docId,
            @RequestParam Integer userId
    ) {
        return ghiChuRepo
                .findTopByTaiLieu_IdAndNguoiDung_IdOrderByCreatedAtDesc(
                        docId, userId
                );
    }

    @PostMapping("/{docId}/canvas")
    public GhiChuTaiLieu saveCanvas(
            @PathVariable Integer docId,
            @RequestParam Integer userId,
            @RequestBody String canvasJson
    ) {
        GhiChuTaiLieu note = new GhiChuTaiLieu();

        note.setTaiLieu(taiLieuChungRepo.getReferenceById(docId));

        NguoiDung nd = new NguoiDung();
        nd.setId(userId);
        note.setNguoiDung(nd);

        note.setCanvasJson(canvasJson);
        note.setCreatedAt(LocalDateTime.now());

        return ghiChuRepo.save(note);
    }

    /* ================= NOTES ================= */

    @GetMapping("/{docId}/notes")
    public List<GhiChuTaiLieu> getNotes(
            @PathVariable Integer  docId,
            @RequestParam Integer userId

    ) {
        return ghiChuRepo
                .findByTaiLieu_IdAndNguoiDung_Id(docId, userId);
    }

    @PostMapping("/{docId}/notes")
    public GhiChuTaiLieu addNote(
            @PathVariable Integer docId,
            @RequestParam Integer userId,
            @RequestBody String content
    ) {
        GhiChuTaiLieu note = new GhiChuTaiLieu();

        note.setTaiLieu(taiLieuChungRepo.getReferenceById(docId));

        NguoiDung nd = new NguoiDung();
        nd.setId(userId);
        note.setNguoiDung(nd);

        note.setNoiDung(content);
        note.setCreatedAt(LocalDateTime.now());

        return ghiChuRepo.save(note);
    }


    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable UUID  id) {
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


}
