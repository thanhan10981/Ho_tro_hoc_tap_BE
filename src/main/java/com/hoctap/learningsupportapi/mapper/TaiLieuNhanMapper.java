package com.hoctap.learningsupportapi.mapper;


import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import com.hoctap.learningsupportapi.model.entity.TaiLieuNhan;
import org.springframework.stereotype.Component;

@Component
public class TaiLieuNhanMapper {

    public PersonalDocResponse toDto(TaiLieuNhan e) {
        PersonalDocResponse dto = new PersonalDocResponse();

        dto.setDocId(e.getTaiLieu().getId());
        dto.setNhanId(e.getNhan().getId());

        dto.setTitle(e.getTaiLieu().getTitle());
        dto.setSubject(e.getTaiLieu().getTitle()); // hoặc field khác nếu có
        dto.setType(e.getTaiLieu().getType());
        dto.setStatus(e.getStatus());

        return dto;
    }
}
