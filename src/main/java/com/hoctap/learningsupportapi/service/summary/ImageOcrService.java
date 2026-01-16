package com.hoctap.learningsupportapi.service.summary;

import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
@Service
@RequiredArgsConstructor
public class ImageOcrService implements FileTextExtractor {

    private final Tesseract tesseract;

    @Override
    public boolean supports(String filename) {
        return filename.endsWith(".png")
                || filename.endsWith(".jpg")
                || filename.endsWith(".jpeg");
    }

    @Override
    public String extract(MultipartFile file) {
        try {
            BufferedImage img = ImageIO.read(file.getInputStream());
            return tesseract.doOCR(img);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi OCR ảnh", e);
        }
    }
}
