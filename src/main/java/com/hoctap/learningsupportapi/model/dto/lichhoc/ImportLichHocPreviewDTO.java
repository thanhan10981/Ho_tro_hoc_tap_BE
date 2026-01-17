package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.*;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportLichHocPreviewDTO {

    private String tenMonHoc;
    private String thu;
    private String gioBatDau;
    private String gioKetThuc;
    private String diaDiem;

    // ===== BỔ SUNG =====
    private String moTa;

    // reminder
    private Boolean nhacTruocBatDau;
    private Integer soPhutTruocBatDau;

    private Boolean nhacTruocKetThuc;
    private Integer soPhutTruocKetThuc;

    // repeat
    private Boolean lapLai;
    private RepeatRuleDTO repeatRule;
}
