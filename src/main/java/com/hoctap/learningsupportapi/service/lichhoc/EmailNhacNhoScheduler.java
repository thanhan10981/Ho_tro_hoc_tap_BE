package com.hoctap.learningsupportapi.service.lichhoc;

import com.hoctap.learningsupportapi.model.dto.lichhoc.EmailNhacNhoDTO;
import com.hoctap.learningsupportapi.model.entity.NhacNho;
import com.hoctap.learningsupportapi.repository.NhacNhoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNhacNhoScheduler {

    private final NhacNhoRepository nhacNhoRepo;
    private final EmailService emailService;

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void guiEmailNhacNho() {

        List<Object[]> rows = nhacNhoRepo.findEmailCanGuiRaw();

        for (Object[] r : rows) {
            try {
                EmailNhacNhoDTO dto = new EmailNhacNhoDTO(
                        ((Number) r[0]).intValue(),  // nhacId
                        (String) r[1],               // tieuDe
                        ((Number) r[2]).intValue(),  // maSuKien
                        (String) r[3],               // hoTen
                        (String) r[4],               // email

                        toLocalDateTime(r[8]),       // thoiGianBatDau
                        toLocalDateTime(r[5]),       // thoiGianKetThuc

                        (String) r[6],               // tenMonHoc
                        (String) r[7],               // loaiSuKien
                        (String) r[9],               // mucDoUuTien
                        (String) r[10],              // diaDiem
                        (String) r[11],              // moTa
                        (Boolean) r[12]              // loaiNhacNho
                );




                emailService.sendNhacNhoEmail(dto);

                NhacNho nhac = nhacNhoRepo.findById(dto.getNhacId()).orElseThrow();
                nhac.setTrangThai("SENT");
                nhac.setNgayGui(LocalDateTime.now());

            } catch (Exception e) {
                log.error("Gửi mail thất bại", e);
            }
        }
    }
    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) return null;

        if (value instanceof LocalDateTime ldt) {
            return ldt;
        }

        if (value instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }

        if (value instanceof String s) {
            return LocalDateTime.parse(s);
        }

        throw new IllegalArgumentException(
                "Không convert được kiểu thời gian: " + value.getClass()
        );
    }
}
