package com.hoctap.learningsupportapi.model.dto;

import com.hoctap.learningsupportapi.model.entity.LuuTaiLieu;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PersonalDocResponse {

    private Long personalId;
    private Integer docId;
    private Integer nhanId;
    private Long size;
    private String title;
    private String subject;
    private String type;
    private String status;
    private LocalDateTime savedAt;

    public static PersonalDocResponse from(LuuTaiLieu luu) {
        PersonalDocResponse dto = new PersonalDocResponse();
        dto.setDocId(luu.getTaiLieu().getId());
        dto.setTitle(luu.getTaiLieu().getTitle());
        dto.setType(luu.getTaiLieu().getType());
        dto.setSize(luu.getTaiLieu().getSize());
        dto.setStatus(luu.getStatus());
        dto.setSavedAt(luu.getSavedAt());
        return dto;
    }

}
