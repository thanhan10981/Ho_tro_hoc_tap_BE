package com.hoctap.learningsupportapi.mapper;

import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import com.hoctap.learningsupportapi.model.entity.LuuTaiLieu;
import org.springframework.stereotype.Component;

@Component
public class LuuTaiLieuMapper {
    public PersonalDocResponse toDto(LuuTaiLieu entity) {
        PersonalDocResponse dto = new PersonalDocResponse();
        dto.setDocId(entity.getTaiLieu().getId());
        dto.setTitle(entity.getTaiLieu().getTitle());
        dto.setSavedAt(entity.getSavedAt());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
