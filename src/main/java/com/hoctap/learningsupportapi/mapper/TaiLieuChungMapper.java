package com.hoctap.learningsupportapi.mapper;

import com.hoctap.learningsupportapi.model.dto.KnowledgeDocResponse;
import com.hoctap.learningsupportapi.model.entity.TaiLieuChung;
import org.springframework.stereotype.Component;

@Component
public class TaiLieuChungMapper {

    public KnowledgeDocResponse toDto(TaiLieuChung e) {
        KnowledgeDocResponse dto = new KnowledgeDocResponse();

        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());

        // subject lấy từ ChuDe
        if (e.getTitle() != null) {
            dto.setSubject(e.getTitle());
        }

        dto.setType(e.getType());
        dto.setNhan(e.getTitle());

        dto.setSize(e.getSize());
        dto.setViews(e.getViews());
        dto.setDownloads(e.getDownloads());
        dto.setCreatedAt(e.getCreatedAt());

        return dto;
    }
}
