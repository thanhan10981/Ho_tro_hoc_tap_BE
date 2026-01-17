package com.hoctap.learningsupportapi.model.dto.summary;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonHocThongKeDTO {
    private Integer maMonHoc;
    private String tenMonHoc;
    private Long soLuongTomTat;
}
