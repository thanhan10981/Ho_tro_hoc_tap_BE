package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateNhacNhoDTO {
    private LocalDateTime thoiGianNhacNho;
}

