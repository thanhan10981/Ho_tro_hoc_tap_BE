package com.hoctap.learningsupportapi.service.lichhoc;




import com.hoctap.learningsupportapi.model.dto.lichhoc.CreateEventRequest;
import com.hoctap.learningsupportapi.model.dto.lichhoc.LichHocCalendarDTO;
import com.hoctap.learningsupportapi.model.entity.LichHoc;
import com.hoctap.learningsupportapi.model.entity.NhacNho;
import com.hoctap.learningsupportapi.repository.LichHocRepository;
import com.hoctap.learningsupportapi.repository.NhacNhoRepository;
import com.hoctap.learningsupportapi.service.AuditLogService;
import com.hoctap.learningsupportapi.service.CurrentUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LichHocServiceImpl implements LichHocService {

    private final LichHocRepository lichHocRepository;
    private final NhacNhoRepository nhacNhoRepo;
    private final CurrentUserService currentUserService;
    private final AuditLogService auditLogService;


    @Override
    public List<LichHocCalendarDTO> getLichHocCuaNguoiDungHienTai() {

        Integer currentUserId = currentUserService.getCurrentUserId();

        List<LichHoc> lichHocList =
                lichHocRepository.findByMaNguoiDung(currentUserId);

        return lichHocList.stream()
                .map(l -> LichHocCalendarDTO.builder()
                        .maSuKien(l.getMaSuKien())
                        .tieuDe(l.getTieuDe())
                        .diaDiem(l.getDiaDiem())
                        .loaiSuKien(l.getLoaiSuKien())

                        .ngayBatDau(
                                l.getThoiGianBatDau() != null
                                        ? l.getThoiGianBatDau().toLocalDate()
                                        : null
                        )
                        .ngayKetThuc(
                                l.getThoiGianKetThuc() != null
                                        ? l.getThoiGianKetThuc().toLocalDate()
                                        : null
                        )
                        .gioBatDau(
                                l.getThoiGianBatDau() != null
                                        ? l.getThoiGianBatDau().toLocalTime()
                                        : null
                        )
                        .gioKetThuc(
                                l.getThoiGianKetThuc() != null
                                        ? l.getThoiGianKetThuc().toLocalTime()
                                        : null
                        )
                        .build())
                .toList();
    }

    @Override
    public List<LichHocCalendarDTO> getLichHocTrongKhoang(String fromDate, String toDate) {

        Integer userId = currentUserService.getCurrentUserId();

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        List<LichHoc> list = lichHocRepository.findByUserAndDateRange(
                userId,
                from.atStartOfDay(),
                to.atTime(23, 59, 59)
        );

        return list.stream()
                .map(l -> LichHocCalendarDTO.builder()
                        .maSuKien(l.getMaSuKien())
                        .tieuDe(l.getTieuDe())
                        .diaDiem(l.getDiaDiem())
                        .loaiSuKien(l.getLoaiSuKien())
                        .ngayBatDau(l.getThoiGianBatDau().toLocalDate())
                        .ngayKetThuc(l.getThoiGianKetThuc().toLocalDate())
                        .gioBatDau(l.getThoiGianBatDau().toLocalTime())
                        .gioKetThuc(l.getThoiGianKetThuc().toLocalTime())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void createEvent(CreateEventRequest request) {

        Integer userId = currentUserService.getCurrentUserId();

        LichHoc event = LichHoc.builder()
                .maNguoiDung(userId)
                .maMonHoc(request.getMaMonHoc())
                .tieuDe(request.getTieuDe())
                .moTa(request.getMoTa())
                .loaiSuKien(request.getLoaiSuKien())
                .thoiGianBatDau(request.getThoiGianBatDau())
                .thoiGianKetThuc(request.getThoiGianKetThuc())
                .mucDoUuTien(request.getMucDoUuTien())
                .diaDiem(request.getDiaDiem())
                .build();

        LichHoc savedEvent = lichHocRepository.saveAndFlush(event);

        if (request.getThoiGianNhacNho() != null) {
            nhacNhoRepo.save(
                    NhacNho.builder()
                            .maSuKien(savedEvent.getMaSuKien())
                            .maNguoiDung(userId)
                            .thoiGianNhacNho(request.getThoiGianNhacNho())
                            .nhacApp(true)          // DEFAULT = 1
                            .nhacEmail(true)        // DEFAULT = 1
                            .trangThai("pending")   // đúng schema
                            .build()
            );
        }

        // ✅ GHI LOG
        auditLogService.log(
                userId,
                "Tạo sự kiện lịch: " + savedEvent.getTieuDe(),
                "thanh_cong"
        );

    }

}
