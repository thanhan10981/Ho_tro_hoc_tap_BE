package com.hoctap.learningsupportapi.service.summary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileTextExtractorRouter {

    private final List<FileTextExtractor> extractors;

    public String extract(MultipartFile file) {
        String name = file.getOriginalFilename().toLowerCase();

        return extractors.stream()
                .filter(e -> e.supports(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không hỗ trợ file: " + name))
                .extract(file);
    }
}
