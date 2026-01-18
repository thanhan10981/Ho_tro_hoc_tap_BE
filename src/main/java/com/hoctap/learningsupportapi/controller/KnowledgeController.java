    package com.hoctap.learningsupportapi.controller;
    import com.hoctap.learningsupportapi.model.dto.KnowledgeDocDetailResponse;
    import com.hoctap.learningsupportapi.model.dto.*;
    import com.hoctap.learningsupportapi.model.entity.*;
    import com.hoctap.learningsupportapi.repository.*;
    import com.hoctap.learningsupportapi.service.CloudinaryService;
    import com.hoctap.learningsupportapi.service.KnowledgeService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import java.net.URL;
    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Map;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;



    @RestController
    @RequestMapping("/api/knowledge")
    @RequiredArgsConstructor
    @CrossOrigin(origins = "http://localhost:5173")
    public class KnowledgeController {

        private final KnowledgeService knowledgeService;
        private final CapBacRepository capBacRepo;
        private final TaiLieuChungRepository taiLieuRepo;
        private final ChuDeRepository chuDeRepo;
        private final LinhVucRepository linhVucRepo;
        private final CloudinaryService cloudinaryService;
        private final NguoiDungRepository nguoiDungRepository;


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
                @RequestParam Integer docId,
                Authentication authentication
        ) {
            String email = authentication.getName(); // lấy từ token
            NguoiDung user = nguoiDungRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            knowledgeService.saveToPersonal(user.getId(), docId);
            return ResponseEntity.ok().build();
        }


        /* ================== UPLOAD ================== */

        @PostMapping("/upload-full")
        public ResponseEntity<?> uploadFull(
                @RequestParam MultipartFile file,
                @RequestParam String title,
                @RequestParam String description,
                @RequestParam Integer capBacId,
                @RequestParam Integer linhVucId,
                @RequestParam Integer chuDeId
        ) {
            try {
                if (file == null || file.isEmpty()) {
                    return ResponseEntity.badRequest().body("File rỗng");
                }

                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body("File vượt quá 10MB");
                }

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !auth.isAuthenticated()
                        || auth.getPrincipal().equals("anonymousUser")) {
                    return ResponseEntity.status(401).body("Chưa đăng nhập");
                }

                String email = auth.getName();
                NguoiDung user = nguoiDungRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User không tồn tại"));

                Map uploadResult = cloudinaryService.uploadFile(file);
                String fileUrl = uploadResult.get("secure_url").toString();

                CapBac capBac = capBacRepo.findById(capBacId).orElseThrow();
                LinhVuc linhVuc = linhVucRepo.findById(linhVucId).orElseThrow();
                ChuDe chuDe = chuDeRepo.findById(chuDeId).orElseThrow();

                TaiLieuChung tl = new TaiLieuChung();
                tl.setTitle(title);
                tl.setDescription(description);
                tl.setFilePath(fileUrl);
                tl.setSize(file.getSize());
                tl.setViews(0);
                tl.setDownloads(0);
                tl.setCreatedAt(LocalDateTime.now());
                tl.setNguoiDung(user);
                tl.setCapBac(capBac);
                tl.setLinhVuc(linhVuc);
                tl.setChuDe(chuDe);

                String ext = file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                tl.setType(ext.toUpperCase());

                return ResponseEntity.ok(taiLieuRepo.save(tl));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Upload thất bại");
            }
        }


        @GetMapping("/preview/{id}")
        public ResponseEntity<byte[]> preview(@PathVariable Integer id) {
            try {
                TaiLieuChung tl = taiLieuRepo.findById(id).orElseThrow();

                if (!"PDF".equalsIgnoreCase(tl.getType())) {
                    return ResponseEntity
                            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .build();
                }

                byte[] bytes = new URL(tl.getFilePath()).openStream().readAllBytes();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.inline().build());

                return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }
        }

        @GetMapping("/preview-office/{id}")
        public ResponseEntity<byte[]> previewOffice(@PathVariable Integer id) {
            try {
                TaiLieuChung tl = taiLieuRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu"));

                // Chỉ cho DOC / DOCX
                if (!tl.getType().equalsIgnoreCase("DOC")
                        && !tl.getType().equalsIgnoreCase("DOCX")) {
                    return ResponseEntity
                            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .build();
                }

                // Lấy file từ Cloudinary
                URL url = new URL(tl.getFilePath());
                byte[] originalBytes;
                try (InputStream in = url.openStream()) {
                    originalBytes = in.readAllBytes();
                }

                // ==== GHI FILE TẠM ====
                String inputExt = tl.getType().toLowerCase();
                java.io.File inputFile = java.io.File.createTempFile("doc-preview-", "." + inputExt);
                java.nio.file.Files.write(inputFile.toPath(), originalBytes);

                java.io.File outputPdf = java.io.File.createTempFile("doc-preview-", ".pdf");

                // ==== CONVERT DOC/DOCX → PDF (LibreOffice) ====
                Process process = new ProcessBuilder(
                        "soffice",
                        "--headless",
                        "--convert-to", "pdf",
                        "--outdir", outputPdf.getParent(),
                        inputFile.getAbsolutePath()
                ).start();

                process.waitFor();

                byte[] pdfBytes = java.nio.file.Files.readAllBytes(outputPdf.toPath());

                // cleanup
                inputFile.delete();
                outputPdf.delete();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.inline().build());

                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }
        }


        @GetMapping("/{id}")
        public KnowledgeDocDetailResponse getDetail(@PathVariable Integer id) {
            return knowledgeService.getDocDetail(id);
        }

        @GetMapping("/cap-bac")
        public List<CapBac> getCapBac() {
            return capBacRepo.findAll();
        }

        @GetMapping("/linh-vuc")
        public List<LinhVuc> getLinhVuc() {
            return linhVucRepo.findAll();
        }

        @GetMapping("/chu-de")
        public List<ChuDe> getChuDe(@RequestParam Integer linhVucId) {
            return chuDeRepo.findByLinhVuc_Id(linhVucId);
        }

        @GetMapping("/{id}/saved")
        public Map<String, Boolean> isSaved(
                @PathVariable Integer id,
                @RequestParam Integer userId
        ) {
            return Map.of("saved", knowledgeService.isSaved(userId, id));
        }


        @GetMapping("/{id}/rating")
        public Map<String, Object> getRating(@PathVariable Integer id) {
            return Map.of(
                    "avg", knowledgeService.getAvgRating(id),
                    "total", knowledgeService.getTotalRating(id)
            );
        }


        @GetMapping("/sidebar/linh-vuc")
        public List<SidebarStatResponse> sidebar() {
            return knowledgeService.sidebarByLinhVuc();
        }

        /* ================== SEARCH ================== */

        @GetMapping("/search")
        public Page<TaiLieuChung> searchFull(
                @RequestParam(required = false) String keyword,
                @RequestParam(required = false) String type,
                @RequestParam(required = false) Integer linhVucId,
                @RequestParam(required = false) Integer chuDeId,
                @RequestParam(required = false) Integer capBacId,
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

            return taiLieuRepo.searchFull(
                    keyword,
                    type,
                    linhVucId,
                    chuDeId,
                    capBacId,
                    rating,
                    pageable
            );
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

        @GetMapping("/download/{id}")
        public ResponseEntity<byte[]> download(@PathVariable Integer id) {
            try {
                TaiLieuChung tl = taiLieuRepo.findById(id)
                        .orElseThrow();

                URL url = new URL(tl.getFilePath());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                byte[] bytes;
                try (InputStream in = conn.getInputStream()) {
                    bytes = in.readAllBytes();
                }

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(getMediaType(tl.getType()));
                headers.setContentDisposition(
                        ContentDisposition.attachment()
                                .filename(tl.getTitle() + "." + tl.getType().toLowerCase())
                                .build()
                );

                return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        private MediaType getMediaType(String type) {
            return switch (type.toUpperCase()) {
                case "PDF" -> MediaType.APPLICATION_PDF;
                case "DOC", "DOCX" ->
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                case "PPT", "PPTX" ->
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
                case "XLS", "XLSX" ->
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                default -> MediaType.APPLICATION_OCTET_STREAM;
            };
        }


    }
