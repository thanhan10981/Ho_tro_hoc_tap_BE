package com.hoctap.learningsupportapi.controller.summary;

import com.hoctap.learningsupportapi.service.summary.TomTatExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/tom-tat/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TomTatExportController {

    private final TomTatExportService exportService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Integer id) {

        ByteArrayOutputStream out =
                exportService.exportToPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=tomtat_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }

    @GetMapping("/{id}/docx")
    public ResponseEntity<byte[]> exportDocx(@PathVariable Integer id) {

        ByteArrayOutputStream out =
                exportService.exportToDocx(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=tomtat_" + id + ".docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(out.toByteArray());
    }
}
