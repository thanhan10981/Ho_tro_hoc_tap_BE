package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHocUpcomingDTO {

    private String tieuDe;
    private String diaDiem;
    private String moTa;
    private String mucDoUuTien;
    private String thoiGianKetThuc;
}
