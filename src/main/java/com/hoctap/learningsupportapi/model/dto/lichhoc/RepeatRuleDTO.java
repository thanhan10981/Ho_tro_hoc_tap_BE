package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Data;

@Data
public class RepeatRuleDTO {
    private String freq;       // DAILY, WEEKLY, MONTHLY
    private Integer interval;
    private Integer count;
    private String until;      // yyyy-MM-dd
}
