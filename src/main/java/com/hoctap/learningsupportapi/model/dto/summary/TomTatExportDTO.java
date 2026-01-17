package com.hoctap.learningsupportapi.model.dto.summary;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TomTatExportDTO {

    private String tieuDe;
    private String noiDung;
    private String tenMonHoc;
    private Integer soTu;
    private Integer soTrang;
    private LocalDateTime ngayTao;
    private List<String> tuKhoa;
}
