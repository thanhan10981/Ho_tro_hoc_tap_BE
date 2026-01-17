package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ImportSchedulePreviewDTO {
    private List<ImportMonHocPreviewDTO> monHocMoi;
    private List<ImportLichHocPreviewDTO> lichHoc;
}
