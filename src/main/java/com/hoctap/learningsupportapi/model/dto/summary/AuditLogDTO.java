package com.hoctap.learningsupportapi.model.dto.summary;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDTO {
    private String moTa;
    private String thoiGian;
}
