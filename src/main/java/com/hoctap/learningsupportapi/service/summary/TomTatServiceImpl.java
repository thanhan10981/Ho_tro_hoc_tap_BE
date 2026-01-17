package com.hoctap.learningsupportapi.service.summary;



import com.hoctap.learningsupportapi.model.dto.summary.*;
import com.hoctap.learningsupportapi.model.entity.TomTatBaiHoc;
import com.hoctap.learningsupportapi.model.entity.TomTatTuKhoa;
import com.hoctap.learningsupportapi.repository.MonHocCaNhanRepository;
import com.hoctap.learningsupportapi.repository.TomTatBaiHocRepository;
import com.hoctap.learningsupportapi.repository.TomTatSpecification;
import com.hoctap.learningsupportapi.repository.TomTatTuKhoaRepository;
import com.hoctap.learningsupportapi.service.*;

import com.hoctap.learningsupportapi.utils.summary.SubTitleExtractor;
import com.hoctap.learningsupportapi.utils.summary.TimeAgoUtil;
import com.hoctap.learningsupportapi.utils.summary.TomTatPromptBuilder;
import com.hoctap.learningsupportapi.utils.summary.TomTatTitlePromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TomTatServiceImpl implements TomTatService {

    private final GeminiService geminiService;
    private final CurrentUserService currentUserService;
    private final AuditLogService auditLogService;
    private final TomTatBaiHocRepository repository;
    private final TomTatKeywordService keywordService;
    private final TomTatTuKhoaRepository tuKhoaRepo;
    private final MonHocCaNhanRepository monHocRepo;
    private final TomTatBaiHocRepository tomTatRepo;

    @Override
    public TomTatPreviewResponse preview(TomTatPreviewRequest req) {

        int soTu = countWords(req.getNoiDung());
        if (soTu > 5000) {
            throw new RuntimeException("Nội dung vượt quá 5000 chữ");
        }

        // TODO: lấy tên môn học nếu cần
        String tenMonHoc = ""; // hoặc load từ MonHocRepository

        String tieuDe;

        if (req.isAutoGenerateTitle()) {
            String titlePrompt = TomTatTitlePromptBuilder.build(req.getNoiDung());
            tieuDe = geminiService.askGemini(titlePrompt);
        } else if (req.getTieuDe() != null && !req.getTieuDe().isBlank()) {
            tieuDe = req.getTieuDe();
        } else {
            // fallback nếu FE không gửi gì
            tieuDe = "Tóm tắt nội dung học tập";
        }


        String prompt = TomTatPromptBuilder.build(req);
        String aiResult = geminiService.askGemini(prompt);

        return TomTatPreviewResponse.builder()
                .tieuDe(tieuDe) // ✅ QUAN TRỌNG
                .noiDungTomTat(aiResult)
                .soTu(soTu)
                .soTrang(estimatePages(soTu))
                .build();
    }


    @Override
    public void confirmAndSave(TomTatConfirmRequest req) {

        Integer userId = currentUserService.getCurrentUserId();

        TomTatBaiHoc entity = TomTatBaiHoc.builder()
                .maNguoiDung(userId)
                .maMonHoc(req.getMaMonHoc())
                .tieuDe(req.getTieuDe())
                .noiDungTomTat(req.getNoiDungTomTat())
                .soTu(req.getSoTu())
                .soTrang(req.getSoTrang())
                .trangThai("hoan_thanh")
                .build();

        repository.save(entity);

        keywordService.generateAndSave(
                entity.getMaTomTat(),
                entity.getNoiDungTomTat()
        );
        auditLogService.log(
                userId,
                "Lưu tóm tắt bài học: " + req.getTieuDe(),
                "thanh_cong"
        );

    }

    private int countWords(String text) {
        return text.trim().split("\\s+").length;
    }

    private int estimatePages(int soTu) {
        return (int) Math.ceil(soTu / 400.0);
    }

    @Override
    public List<TomTatViewDTO> getDanhSachTomTatDaLuu() {

        Integer userId = currentUserService.getCurrentUserId();

        List<TomTatBaiHoc> danhSach =
                tomTatRepo.findByMaNguoiDungOrderByNgayTaoDesc(userId);

        return danhSach.stream()
                .map(this::toDTO)
                .toList();
    }

    private TomTatViewDTO toDTO(TomTatBaiHoc entity) {

        // Môn học
        MonHocDTO monHocDTO = null;
        if (entity.getMaMonHoc() != null) {
            monHocDTO = monHocRepo
                    .findById(entity.getMaMonHoc())
                    .map(monHoc -> new MonHocDTO(
                            monHoc.getMaMonHoc(),
                            monHoc.getTenMonHoc()
                    ))
                    .orElse(null);
        }

        // Từ khóa
        List<String> tuKhoa = tuKhoaRepo
                .findByMaTomTat(entity.getMaTomTat())
                .stream()
                .map(TomTatTuKhoa::getTuKhoa)
                .toList();

        return TomTatViewDTO.builder()
                .maTomTat(entity.getMaTomTat())
                .tieuDe(entity.getTieuDe())
                .subTieuDe(
                        SubTitleExtractor.extract(entity.getNoiDungTomTat())
                )
                .monHoc(monHocDTO)
                .tuKhoa(tuKhoa)
                .soTu(entity.getSoTu())
                .soTrang(entity.getSoTrang())
                .thoiGianTao(
                        TimeAgoUtil.format(entity.getNgayTao())
                )
                .noiDung(entity.getNoiDungTomTat())
                .tomTatDayDu(entity.getNoiDungTomTat())
                .build();
    }

    @Override
    public List<TomTatViewDTO> filterTomTat(TomTatFilterRequest req) {

        Integer userId = currentUserService.getCurrentUserId();

        Specification<TomTatBaiHoc> spec = Specification
                .where(TomTatSpecification.byUser(userId))
                .and(TomTatSpecification.keyword(req.getKeyword()))
                .and(TomTatSpecification.monHoc(req.getMaMonHoc()))
                .and(TomTatSpecification.dateRange(req.getFromDate(), req.getToDate()))
                .and(TomTatSpecification.soTrang(req.getMinSoTrang(), req.getMaxSoTrang()))
                .and(TomTatSpecification.soTu(req.getMinSoTu(), req.getMaxSoTu()));

        Sort sort = buildSort(req.getSortType());

        return tomTatRepo.findAll(spec, sort)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private Sort buildSort(TomTatSortType type) {
        if (type == null) {
            return Sort.by(Sort.Direction.DESC, "ngayTao");
        }

        return switch (type) {
            case MOI_NHAT -> Sort.by(Sort.Direction.DESC, "ngayTao");
            case CU_NHAT -> Sort.by(Sort.Direction.ASC, "ngayTao");
            case TEN_A_Z -> Sort.by(Sort.Direction.ASC, "tieuDe");
            case SO_TRANG_CAO_NHAT -> Sort.by(Sort.Direction.DESC, "soTrang");
            case SO_TRANG_THAP_NHAT -> Sort.by(Sort.Direction.ASC, "soTrang");
            case SO_TU_NHIEU_NHAT -> Sort.by(Sort.Direction.DESC, "soTu");
            case SO_TU_IT_NHAT -> Sort.by(Sort.Direction.ASC, "soTu");
        };
    }

    @Override
    public MonHocThongKeDTO getMonHocNhieuTomTatNhatTuan() {

        Integer userId = currentUserService.getCurrentUserId();

        LocalDateTime startOfWeek = LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .atStartOfDay();

        List<MonHocThongKeDTO> list =
                tomTatRepo.thongKeTomTatTheoMonTrongTuan(startOfWeek, userId);

        return list.isEmpty() ? null : list.get(0);
    }

}
