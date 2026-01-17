package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Data;

@Data
public class UpdateReminderEmailRequest {
    private String emailMoi;
    private String otp;
}

