package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Data;
import java.util.List;

@Data
public class GeminiImportScheduleDTO {

    private List<SubjectDTO> subjects;

    @Data
    public static class SubjectDTO {
        private String tenMonHoc;
        private List<ScheduleDTO> lich;
    }

    @Data
    public static class ScheduleDTO {
        private String thu;          // "Thứ 2"
        private String gioBatDau;    // "07:30"
        private String gioKetThuc;   // "09:30"
        private String diaDiem;
    }
}
