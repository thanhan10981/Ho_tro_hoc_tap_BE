package com.hoctap.learningsupportapi.mapper;

import com.hoctap.learningsupportapi.model.dto.KnowledgeDocResponse;
import com.hoctap.learningsupportapi.model.entity.TaiLieuChung;
import com.hoctap.learningsupportapi.repository.DanhGiaTaiLieuChungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaiLieuChungMapper {

    private final DanhGiaTaiLieuChungRepository danhGiaRepo;

    public KnowledgeDocResponse toDto(TaiLieuChung e) {
        KnowledgeDocResponse dto = new KnowledgeDocResponse();

        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());

        // ✅ CHỦ ĐỀ
        if (e.getChuDe() != null) {
            dto.setSubject(e.getChuDe().getTenChuDe());
        }

        // ✅ LĨNH VỰC
        if (e.getLinhVuc() != null) {
            dto.setLinhVuc(e.getLinhVuc().getTenLinhVuc());
        }

        dto.setType(e.getType());
        dto.setSize(e.getSize());
        dto.setViews(e.getViews());
        dto.setDownloads(e.getDownloads());
        dto.setCreatedAt(e.getCreatedAt());

        // ✅ ĐÁNH GIÁ SAO
        dto.setRating(danhGiaRepo.avgRating(e.getId()));

        return dto;
    }
}
