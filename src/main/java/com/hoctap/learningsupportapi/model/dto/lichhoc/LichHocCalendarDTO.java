package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHocCalendarDTO {

    private Integer maSuKien;
    private String tieuDe;
    private String diaDiem;
    private String loaiSuKien;

    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
}
