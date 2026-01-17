    package com.hoctap.learningsupportapi.controller;
    import com.hoctap.learningsupportapi.model.dto.KnowledgeDocResponse;
    import com.hoctap.learningsupportapi.model.entity.GhiChuTaiLieu;
    import com.hoctap.learningsupportapi.model.entity.TaiLieuChung;
    import com.hoctap.learningsupportapi.model.entity.TaiLieuNhan;
    import com.hoctap.learningsupportapi.repository.GhiChuTaiLieuRepository;
    import com.hoctap.learningsupportapi.repository.TaiLieuChungRepository;
    import com.hoctap.learningsupportapi.repository.TaiLieuNhanRepository;
    import com.hoctap.learningsupportapi.service.KnowledgeService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.core.io.Resource;
    import org.springframework.core.io.UrlResource;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;
    @RestController
    @RequestMapping("/api/knowledge")
    @RequiredArgsConstructor
    public class KnowledgeController {

        private final KnowledgeService knowledgeService;
        private final GhiChuTaiLieuRepository noteRepo;
        private final TaiLieuChungRepository taiLieuRepo;
        private final TaiLieuNhanRepository taiLieuNhanRepo;

        private static final String UPLOAD_DIR = "uploads";

        /* ================== KHO CHUNG ================== */

        @GetMapping("/common")
        public Page<KnowledgeDocResponse> getCommon(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "6") int size
        ) {
            return knowledgeService.getCommonDocs(page, size);
        }

        @PostMapping("/save")
        public ResponseEntity<?> saveToPersonal(
                @RequestParam Integer userId,
                @RequestParam Integer docId
        ) {
            knowledgeService.saveToPersonal(userId, docId);
            return ResponseEntity.ok().build();
        }

        /* ================== GHI CHÚ ================== */

        @GetMapping("/notes")
        public List<GhiChuTaiLieu> getNotes(
                @RequestParam Integer docId,
                @RequestParam Integer userId
        ) {
            return noteRepo.findByTaiLieu_IdAndNguoiDung_Id(docId, userId);
        }

        @PostMapping("/notes")
        public GhiChuTaiLieu addNote(@RequestBody GhiChuTaiLieu note) {
            note.setCreatedAt(LocalDateTime.now());
            return noteRepo.save(note);
        }

        @DeleteMapping("/notes/{id}")
        public ResponseEntity<?> deleteNote(@PathVariable UUID id) {
            noteRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }

        /* ================== UPLOAD ================== */

        @PostMapping("/upload-full")
        public ResponseEntity<TaiLieuChung> uploadFull(
                @RequestParam MultipartFile file,
                @RequestParam String title,
                @RequestParam String description
        ) throws IOException {

            if (file.isEmpty()) {
                throw new RuntimeException("File rỗng");
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                throw new RuntimeException("Chỉ cho phép file PDF");
            }

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String filename = UUID.randomUUID() + ".pdf";
            Path path = Paths.get(UPLOAD_DIR, filename);

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            TaiLieuChung tl = new TaiLieuChung();
            tl.setTitle(title);
            tl.setDescription(description);
            tl.setFilePath(path.toString());
            tl.setType("PDF");
            tl.setSize((long) file.getSize());
            tl.setViews(0);
            tl.setDownloads(0);
            tl.setCreatedAt(LocalDateTime.now());

            return ResponseEntity.ok(taiLieuRepo.save(tl));
        }

        /* ================== SEARCH ================== */

        @GetMapping("/search")
        public Page<TaiLieuChung> searchFull(
                @RequestParam(required = false) String keyword,
                @RequestParam(required = false) String type,
                @RequestParam(required = false) String subject,
                @RequestParam(required = false) Integer rating,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "6") int size,
                @RequestParam(defaultValue = "createdAt") String sort
        ) {
            Pageable pageable;

            try {
                pageable = PageRequest.of(page, size, Sort.by(sort).descending());
            } catch (Exception e) {
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            }

            return taiLieuRepo.searchFull(keyword, type, subject, rating, pageable);
        }

        /* ================== VIEW / DOWNLOAD ================== */

        @PostMapping("/view/{id}")
        public ResponseEntity<?> increaseView(@PathVariable Integer id) {
            TaiLieuChung tl = taiLieuRepo.findById(id).orElseThrow();
            tl.setViews(tl.getViews() + 1);
            taiLieuRepo.save(tl);
            return ResponseEntity.ok().build();
        }

        @PostMapping("/download/{id}")
        public ResponseEntity<?> increaseDownload(@PathVariable Integer id) {
            TaiLieuChung tl = taiLieuRepo.findById(id).orElseThrow();
            tl.setDownloads(tl.getDownloads() + 1);
            taiLieuRepo.save(tl);
            return ResponseEntity.ok().build();
        }

        /* ================== PREVIEW PDF ================== */

        @GetMapping("/preview/{id}")
        public ResponseEntity<Resource> preview(@PathVariable Integer id) throws IOException {

            TaiLieuChung tl = taiLieuRepo.findById(id).orElseThrow();
            Path path = Paths.get(tl.getFilePath());

            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"preview.pdf\"")
                    .body(resource);
        }

        /* ================== KHO CÁ NHÂN ================== */

        @GetMapping("/personal")
        public List<TaiLieuNhan> getPersonalDocs(@RequestParam Integer userId) {
            return taiLieuNhanRepo.findByNguoiDung_Id(userId);
        }

        @DeleteMapping("/personal")
        public ResponseEntity<?> removeFromPersonal(
                @RequestParam Integer userId,
                @RequestParam Integer docId
        ) {
            taiLieuNhanRepo.deleteByNguoiDung_IdAndTaiLieu_Id(userId, docId);
            return ResponseEntity.ok().build();
        }


    }
