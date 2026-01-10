package com.hoctap.learningsupportapi.model.dto.lichhoc;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MonHocResponseDTO {

    private Integer maMonHoc;
    private String tenMonHoc;
    private String moTa;
    private String mucDoHoc;
    private LocalDateTime ngayTao;
}
