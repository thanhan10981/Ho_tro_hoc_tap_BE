package com.hoctap.learningsupportapi.service.lichhoc;

import com.hoctap.learningsupportapi.model.dto.lichhoc.EmailNhacNhoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;



    private String buildEmailContent(EmailNhacNhoDTO d) {

        if (Boolean.TRUE.equals(d.getLoaiNhacNho())) {
            return buildKetThucContent(d);
        }
        return buildBatDauContent(d);
    }

    private String buildBatDauContent(EmailNhacNhoDTO d) {
        return String.format("""
        Xin chào %s,

        ⏰ Sự kiện "%s" sắp bắt đầu.

        🕒 Thời gian bắt đầu: %s

        Thông tin chi tiết:
        - Môn học: %s
        - Loại sự kiện: %s
        - Địa điểm: %s
        - Mức độ ưu tiên: %s
        - Mô tả: %s

        Hãy chuẩn bị để tham gia đúng giờ nhé!
        """,
                d.getHoTen(),
                d.getTieuDe(),
                d.getThoiGianBatDau(),
                d.getTenMonHoc(),
                d.getLoaiSuKien(),
                d.getDiaDiem(),
                d.getMucDoUuTien(),
                d.getMoTa()
        );
    }

    private String buildKetThucContent(EmailNhacNhoDTO d) {
        return String.format("""
        Xin chào %s,

        ⚠️ Sự kiện "%s" sắp kết thúc.

        🕒 Thời gian kết thúc: %s

        Thông tin chi tiết:
        - Môn học: %s
        - Loại sự kiện: %s
        - Địa điểm: %s
        - Mức độ ưu tiên: %s
        - Mô tả: %s

        Vui lòng hoàn thành đúng hạn nhé!
        """,
                d.getHoTen(),
                d.getTieuDe(),
                d.getThoiGianKetThuc(),
                d.getTenMonHoc(),
                d.getLoaiSuKien(),
                d.getDiaDiem(),
                d.getMucDoUuTien(),
                d.getMoTa()
        );
    }


    private String buildSubject(EmailNhacNhoDTO d) {
        return d.getLoaiNhacNho()
                ? "⏰ Nhắc sắp kết thúc: " + d.getTieuDe()
                : "📌 Nhắc sắp bắt đầu: " + d.getTieuDe();
    }

    @Override
    public void sendNhacNhoEmail(EmailNhacNhoDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getEmail());
        message.setSubject(buildSubject(dto));
        message.setText(buildEmailContent(dto));
        mailSender.send(message);
    }

}
