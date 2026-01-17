package com.hoctap.learningsupportapi.model.dto.lichhoc;
import lombok.*;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ImportScheduleConfirmRequest {
    private List<ImportLichHocPreviewDTO> lichHoc;
}
