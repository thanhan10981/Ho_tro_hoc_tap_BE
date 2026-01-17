package com.hoctap.learningsupportapi.utils;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class OcrUtils {

    public String extractText(MultipartFile file) {
        try {
            File temp = File.createTempFile("tkb-", ".png");
            file.transferTo(temp);

            Tesseract tesseract = new Tesseract();
            tesseract.setLanguage("vie");

            return tesseract.doOCR(temp);
        } catch (Exception e) {
            throw new RuntimeException("OCR thất bại", e);
        }
    }
}
