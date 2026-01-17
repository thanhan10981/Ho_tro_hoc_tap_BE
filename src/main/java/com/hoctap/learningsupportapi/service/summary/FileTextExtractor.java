package com.hoctap.learningsupportapi.service.summary;

import org.springframework.web.multipart.MultipartFile;

public interface FileTextExtractor {
    boolean supports(String filename);
    String extract(MultipartFile file);
}
