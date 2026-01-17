package com.hoctap.learningsupportapi.model.dto.summary;

import com.hoctap.learningsupportapi.model.dto.lichhoc.MonHocResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TomTatViewDTO {

    private Integer maTomTat;
    private String tieuDe;
    private String subTieuDe;

    private MonHocDTO monHoc;

    private List<String> tuKhoa;

    private Integer soTu;
    private Integer soTrang;

    private String thoiGianTao; // "3 ngày trước"

    private String noiDung;
    private String tomTatDayDu;
}
