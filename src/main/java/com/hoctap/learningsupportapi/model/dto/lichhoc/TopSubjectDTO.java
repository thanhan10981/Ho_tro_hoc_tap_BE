package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSubjectDTO {
    private Integer maMonHoc;
    private String tenMonHoc;
    private Long totalEvents;
}
