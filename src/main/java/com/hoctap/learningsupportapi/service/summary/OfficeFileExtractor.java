package com.hoctap.learningsupportapi.service.summary;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.stream.Collectors;
@Service
public class OfficeFileExtractor implements FileTextExtractor {

    @Override
    public boolean supports(String filename) {
        String name = filename.toLowerCase();
        return name.endsWith(".docx") || name.endsWith(".pptx");
    }

    @Override
    public String extract(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {

            String name = file.getOriginalFilename().toLowerCase();

            if (name.endsWith(".docx")) {
                XWPFDocument doc = new XWPFDocument(is);
                return doc.getParagraphs()
                        .stream()
                        .map(XWPFParagraph::getText)
                        .collect(Collectors.joining("\n"));
            }

            if (name.endsWith(".pptx")) {
                XMLSlideShow ppt = new XMLSlideShow(is);
                StringBuilder sb = new StringBuilder();
                for (XSLFSlide slide : ppt.getSlides()) {
                    for (XSLFShape shape : slide.getShapes()) {
                        if (shape instanceof XSLFTextShape text) {
                            sb.append(text.getText()).append("\n");
                        }
                    }
                }
                return sb.toString();
            }

            throw new RuntimeException("Định dạng Office không hỗ trợ");

        } catch (Exception e) {
            throw new RuntimeException("Lỗi đọc file Office", e);
        }
    }
}
