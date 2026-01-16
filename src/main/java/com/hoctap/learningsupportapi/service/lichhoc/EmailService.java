package com.hoctap.learningsupportapi.service.lichhoc;

import com.hoctap.learningsupportapi.model.dto.lichhoc.EmailNhacNhoDTO;

public interface EmailService {
    void sendNhacNhoEmail(EmailNhacNhoDTO dto);
}
