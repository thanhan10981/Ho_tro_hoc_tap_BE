package com.hoctap.learningsupportapi.model.dto.lichhoc;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateReminderRequest {

    private Integer maSuKien;
    private Boolean loaiNhacNho; // false = bắt đầu, true = kết thúc
    private LocalDateTime thoiGianNhacNhoMoi;
}
