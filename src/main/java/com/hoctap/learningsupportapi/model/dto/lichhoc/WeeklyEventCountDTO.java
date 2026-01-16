package com.hoctap.learningsupportapi.model.dto.lichhoc;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyEventCountDTO {
    private Integer week;
    private Integer year;
    private Long totalEvents;
}
