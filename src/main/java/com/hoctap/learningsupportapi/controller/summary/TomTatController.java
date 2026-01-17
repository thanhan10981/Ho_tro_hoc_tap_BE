package com.hoctap.learningsupportapi.controller.summary;


import com.hoctap.learningsupportapi.model.dto.summary.*;
import com.hoctap.learningsupportapi.service.GeminiService;
import com.hoctap.learningsupportapi.service.summary.FileTextExtractorRouter;
import com.hoctap.learningsupportapi.service.summary.SummaryInputAssembler;
import com.hoctap.learningsupportapi.service.summary.TomTatService;
import com.hoctap.learningsupportapi.utils.summary.TomTatTitlePromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tom-tat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TomTatController {

    private final TomTatService tomTatService;
    private final FileTextExtractorRouter fileTextExtractor;
    private final SummaryInputAssembler assembler;
    private final GeminiService geminiService;
    @PostMapping("/preview")
    public ResponseEntity<TomTatPreviewResponse> preview(
            @RequestBody TomTatPreviewRequest request
    ) {
        return ResponseEntity.ok(tomTatService.preview(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(
            @RequestBody TomTatConfirmRequest request
    ) {
        tomTatService.confirmAndSave(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/preview-file")
    public ResponseEntity<TomTatPreviewResponse> previewFile(
            @RequestParam MultipartFile file,
            @RequestParam String doDai,
            @RequestParam Integer maMonHoc,
            @RequestParam boolean highlightTuKhoa,
            @RequestParam boolean themViDu,
            @RequestParam boolean taoCauHoiOnTap
    ) {

        String extractedText = fileTextExtractor.extract(file);

        TomTatPreviewRequest req = new TomTatPreviewRequest();
        req.setNoiDung(extractedText);
        req.setDoDai(doDai);
        req.setMaMonHoc(maMonHoc);
        req.setHighlightTuKhoa(highlightTuKhoa);
        req.setThemViDu(themViDu);
        req.setTaoCauHoiOnTap(taoCauHoiOnTap);

        return ResponseEntity.ok(tomTatService.preview(req));
    }

    @PostMapping(value = "/preview-mix", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TomTatPreviewResponse> previewMix(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String noiDungText,
            @RequestParam(required = false) List<MultipartFile> files,
            @RequestParam String doDai,
            @RequestParam Integer maMonHoc,
            @RequestParam boolean highlightTuKhoa,
            @RequestParam boolean themViDu,
            @RequestParam boolean taoCauHoiOnTap
    ) {

        String fullContent = assembler.assemble(noiDungText, files);

        TomTatPreviewRequest req = new TomTatPreviewRequest();
        req.setTieuDe(title);
        req.setNoiDung(fullContent);
        req.setDoDai(doDai);
        req.setMaMonHoc(maMonHoc);
        req.setHighlightTuKhoa(highlightTuKhoa);
        req.setThemViDu(themViDu);
        req.setTaoCauHoiOnTap(taoCauHoiOnTap);

        return ResponseEntity.ok(tomTatService.preview(req));
    }

    @PostMapping("/regenerate-title")
    public String regenerateTitle(@RequestBody Map<String, String> body) {
        String noiDung = body.get("noiDung");

        if (noiDung == null || noiDung.isBlank()) {
            throw new IllegalArgumentException("Nội dung trống");
        }

        String prompt = TomTatTitlePromptBuilder.build(noiDung);
        return geminiService.askGemini(prompt);
    }


    @GetMapping("/da-luu")
    public ResponseEntity<List<TomTatViewDTO>> getTomTatDaLuu() {
        return ResponseEntity.ok(
                tomTatService.getDanhSachTomTatDaLuu()
        );
    }

    @PostMapping("/filter")
    public List<TomTatViewDTO> filter(@RequestBody TomTatFilterRequest req) {
        return tomTatService.filterTomTat(req);
    }

    @GetMapping("/top-subject-this-week")
    public MonHocThongKeDTO getTopSubjectThisWeek() {
        return tomTatService.getMonHocNhieuTomTatNhatTuan();
    }

}
