package com.hoctap.learningsupportapi.service.summary;

import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class PdfExtractor implements FileTextExtractor {

    private final Tesseract tesseract;

    @Override
    public boolean supports(String filename) {
        return filename.toLowerCase().endsWith(".pdf");
    }

    @Override
    public String extract(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();

            try (PDDocument doc = Loader.loadPDF(bytes)) {

                // 1️⃣ Thử đọc text gốc
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(doc);

                if (text != null && text.trim().length() > 100) {
                    return text;
                }

                // 2️⃣ PDF scan → OCR
                PDFRenderer renderer = new PDFRenderer(doc);
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < doc.getNumberOfPages(); i++) {
                    BufferedImage img = renderer.renderImageWithDPI(i, 300);
                    sb.append(tesseract.doOCR(img)).append("\n");
                }

                return sb.toString();
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc PDF (PDFBox 3.x)", e);
        }
    }
}
