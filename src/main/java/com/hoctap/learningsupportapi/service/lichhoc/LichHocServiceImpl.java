package com.hoctap.learningsupportapi.service.lichhoc;

import com.hoctap.learningsupportapi.model.dto.lichhoc.*;
import com.hoctap.learningsupportapi.model.entity.LichHoc;
import com.hoctap.learningsupportapi.model.entity.MonHocCaNhan;
import com.hoctap.learningsupportapi.model.entity.NguoiDung;
import com.hoctap.learningsupportapi.model.entity.NhacNho;
import com.hoctap.learningsupportapi.repository.LichHocRepository;
import com.hoctap.learningsupportapi.repository.MonHocCaNhanRepository;
import com.hoctap.learningsupportapi.repository.NguoiDungRepository;
import com.hoctap.learningsupportapi.repository.NhacNhoRepository;
import com.hoctap.learningsupportapi.service.AuditLogService;
import com.hoctap.learningsupportapi.service.CurrentUserService;
import com.hoctap.learningsupportapi.service.GeminiService;
import com.hoctap.learningsupportapi.utils.LichHocTimeFormatter;
import com.hoctap.learningsupportapi.utils.OcrUtils;
import com.hoctap.learningsupportapi.utils.ThuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.tess4j.Tesseract;


import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LichHocServiceImpl implements LichHocService {

    private static final String STATUS_PENDING = "PENDING";

    private final LichHocRepository lichHocRepository;
    private final NhacNhoRepository nhacNhoRepo;
    private final CurrentUserService currentUserService;
    private final AuditLogService auditLogService;
    private final NguoiDungRepository nguoiDungRepository;
    private final MonHocCaNhanRepository monHocCaNhanRepository;
    private final OcrUtils ocrUtils;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    private final Tesseract tesseract;

    // ===================== GET LỊCH =====================

    @Override
    public List<LichHocCalendarDTO> getLichHocCuaNguoiDungHienTai() {

        Integer userId = currentUserService.getCurrentUserId();

        return lichHocRepository.findByMaNguoiDung(userId)
                .stream()
                .map(l -> LichHocCalendarDTO.builder()
                        .maSuKien(l.getMaSuKien())
                        .tieuDe(l.getTieuDe())
                        .diaDiem(l.getDiaDiem())
                        .loaiSuKien(l.getLoaiSuKien())
                        .ngayBatDau(l.getThoiGianBatDau() != null ? l.getThoiGianBatDau().toLocalDate() : null)
                        .ngayKetThuc(l.getThoiGianKetThuc() != null ? l.getThoiGianKetThuc().toLocalDate() : null)
                        .gioBatDau(l.getThoiGianBatDau() != null ? l.getThoiGianBatDau().toLocalTime() : null)
                        .gioKetThuc(l.getThoiGianKetThuc() != null ? l.getThoiGianKetThuc().toLocalTime() : null)
                        .build())
                .toList();
    }

    @Override
    public List<LichHocCalendarDTO> getLichHocTrongKhoang(String fromDate, String toDate) {

        Integer userId = currentUserService.getCurrentUserId();

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        return lichHocRepository.findByUserAndDateRange(
                        userId,
                        from.atStartOfDay(),
                        to.atTime(23, 59, 59)
                )
                .stream()
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

    // ===================== CREATE EVENT =====================

    @Override
    @Transactional
    public void createEvent(CreateEventRequest request) {

        Integer userId = currentUserService.getCurrentUserId();

        String emailMacDinh = nguoiDungRepository.findById(userId)
                .map(NguoiDung::getEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy email người dùng"));

        LichHoc savedEvent = lichHocRepository.saveAndFlush(
                LichHoc.builder()
                        .maNguoiDung(userId)
                        .maMonHoc(request.getMaMonHoc())
                        .tieuDe(request.getTieuDe())
                        .moTa(request.getMoTa())
                        .loaiSuKien(request.getLoaiSuKien())
                        .thoiGianBatDau(request.getThoiGianBatDau())
                        .thoiGianKetThuc(request.getThoiGianKetThuc())
                        .mucDoUuTien(request.getMucDoUuTien())
                        .diaDiem(request.getDiaDiem())
                        .build()
        );

        createRemindersForEvent(savedEvent, request, emailMacDinh);

        auditLogService.log(
                userId,
                "Tạo sự kiện lịch: " + savedEvent.getTieuDe(),
                "thanh_cong"
        );
    }
    private void createRemindersForEvent(
            LichHoc event,
            CreateEventRequest request,
            String email
    ) {
        Integer userId = event.getMaNguoiDung();

        if (Boolean.TRUE.equals(request.getNhacTruocBatDau())) {

            Integer minutes = request.getSoPhutTruocBatDau() != null
                    ? request.getSoPhutTruocBatDau()
                    : 15;

            LocalDateTime tg = event.getThoiGianBatDau()
                    .minusMinutes(minutes);

            nhacNhoRepo.save(
                    buildReminder(
                            event.getMaSuKien(),
                            userId,
                            false,
                            tg,
                            email
                    )
            );
        }

        if (Boolean.TRUE.equals(request.getNhacTruocKetThuc())) {

            Integer minutes = request.getSoPhutTruocKetThuc() != null
                    ? request.getSoPhutTruocKetThuc()
                    : 15;

            LocalDateTime tg = event.getThoiGianKetThuc()
                    .minusMinutes(minutes);

            nhacNhoRepo.save(
                    buildReminder(
                            event.getMaSuKien(),
                            userId,
                            true,
                            tg,
                            email
                    )
            );
        }
    }

    // ===================== UPDATE REMINDER =====================



    // ===================== EMAIL REMINDER =====================

    @Override
    @Transactional
    public void updateReminderEmailForUser(String emailMoi) {

        Integer userId = currentUserService.getCurrentUserId();



        nhacNhoRepo.updateEmailByUser(userId, emailMoi);

        auditLogService.log(
                userId,
                "Cập nhật email nhắc nhở cho toàn bộ sự kiện",
                "thanh_cong"
        );
    }

    @Override
    public String getReminderEmailForUser() {

        Integer userId = currentUserService.getCurrentUserId();

        return nhacNhoRepo.findFirstEmailByUserId(userId)
                .orElseGet(() ->
                        nguoiDungRepository.findById(userId)
                                .map(NguoiDung::getEmail)
                                .orElse(null)
                );
    }

    // ===================== HELPER =====================

    private NhacNho buildReminder(
            Integer maSuKien,
            Integer maNguoiDung,
            boolean loaiNhacNho,
            LocalDateTime thoiGian,
            String email
    )
    {
        if (thoiGian == null) {
            throw new IllegalArgumentException("Thời gian nhắc nhở không hợp lệ");
        }
        return NhacNho.builder()
                .maSuKien(maSuKien)
                .maNguoiDung(maNguoiDung)
                .loaiNhacNho(loaiNhacNho)
                .thoiGianNhacNho(thoiGian)
                .email(email)
                .nhacApp(true)
                .nhacEmail(true)
                .trangThai(STATUS_PENDING)
                .build();
    }

    private LocalDateTime calcReminderTime(
            LocalDateTime base,
            Integer minusMinutes
    ) {
        if (base == null || minusMinutes == null) {
            return null;
        }
        return base.minusMinutes(minusMinutes);
    }

    // ===================== UPDATE SỰ KIỆN =====================
    @Override
    public void updateLichHoc(Integer maSuKien, UpdateLichHocDTO dto) {

        Integer userId = currentUserService.getCurrentUserId();

        LichHoc lichHoc = lichHocRepository
                .findByMaSuKienAndMaNguoiDung(maSuKien, userId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy sự kiện hoặc không có quyền")
                );

        lichHoc.setTieuDe(dto.getTieuDe());
        lichHoc.setDiaDiem(dto.getDiaDiem());
        lichHoc.setLoaiSuKien(dto.getLoaiSuKien());
        lichHoc.setMucDoUuTien(dto.getMucDoUuTien());
        lichHoc.setMoTa(dto.getMoTa());

        LocalDateTime start = LocalDateTime.of(
                dto.getNgayBatDau(),
                dto.getGioBatDau()
        );

        LocalDateTime end = LocalDateTime.of(
                dto.getNgayKetThuc(),
                dto.getGioKetThuc()
        );

        if (end.isBefore(start)) {
            throw new RuntimeException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }

        lichHoc.setThoiGianBatDau(start);
        lichHoc.setThoiGianKetThuc(end);

        lichHocRepository.save(lichHoc);
    }

    // ===================== UPDATE NHẮC NHỞ =====================
    @Override
    public void updateNhacNho(
            Integer maSuKien,
            Boolean loaiNhacNho,
            UpdateNhacNhoDTO dto
    ) {
        Integer userId = currentUserService.getCurrentUserId();

        NhacNho nhacNho = nhacNhoRepo
                .findByMaSuKienAndMaNguoiDungAndLoaiNhacNho(
                        maSuKien,
                        userId,
                        loaiNhacNho
                )
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhắc nhở")
                );

        nhacNho.setThoiGianNhacNho(dto.getThoiGianNhacNho());
        nhacNhoRepo.save(nhacNho);
    }

    // ===================== DELETE NHẮC NHỞ =====================
    @Override
    public void deleteNhacNho(Integer maSuKien, Boolean loaiNhacNho) {

        Integer userId = currentUserService.getCurrentUserId();

        NhacNho nhacNho = nhacNhoRepo
                .findByMaSuKienAndMaNguoiDungAndLoaiNhacNho(
                        maSuKien,
                        userId,
                        loaiNhacNho
                )
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhắc nhở để xóa")
                );

        nhacNhoRepo.delete(nhacNho);
    }

    // ===================== DELETE SỰ KIỆN =====================
    @Override
    @Transactional
    public void deleteLichHoc(Integer maSuKien) {

        Integer userId = currentUserService.getCurrentUserId();

        LichHoc lichHoc = lichHocRepository
                .findByMaSuKienAndMaNguoiDung(maSuKien, userId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy sự kiện hoặc không có quyền xóa")
                );

        // 🔥 1. XÓA NHẮC NHỞ TRƯỚC
        nhacNhoRepo.deleteByMaSuKienAndMaNguoiDung(maSuKien, userId);

        // 🔥 2. XÓA SỰ KIỆN
        lichHocRepository.delete(lichHoc);

        // 🔥 3. LOG
        auditLogService.log(
                userId,
                "Xóa sự kiện lịch: " + lichHoc.getTieuDe(),
                "thanh_cong"
        );
    }
    @Override
    public List<LichHocCalendarDTO> searchLichHoc(LichHocSearchRequest request) {

        Integer userId = currentUserService.getCurrentUserId();

        return lichHocRepository.search(
                        userId,
                        normalize(request.getKeyword()),
                        request.getMaMonHoc(),
                        request.getLoaiSuKien()
                )
                .stream()
                .map(l -> LichHocCalendarDTO.builder()
                        .maSuKien(l.getMaSuKien())
                        .tieuDe(l.getTieuDe())
                        .diaDiem(l.getDiaDiem())
                        .loaiSuKien(l.getLoaiSuKien())
                        .ngayBatDau(l.getThoiGianBatDau().toLocalDate())
                        .ngayKetThuc(l.getThoiGianKetThuc().toLocalDate())
                        .gioBatDau(l.getThoiGianBatDau().toLocalTime())
                        .gioKetThuc(l.getThoiGianKetThuc().toLocalTime())
                        .build()
                )
                .toList();
    }

    private String normalize(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return keyword.trim();
    }
    @Override
    public WeeklyEventCountDTO countWeeklyEvents(Integer year, Integer week) {
        Integer userId = currentUserService.getCurrentUserId();

        // Dùng chuẩn ISO: Thứ 2 = 1, Chủ Nhật = 7
        WeekFields weekFields = WeekFields.ISO;

        // Ngày đầu tuần (Thứ 2) của tuần ISO
        LocalDate startDate = LocalDate
                .ofYearDay(year, 1)
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 1); // 1 = Thứ 2

        // Ngày cuối tuần = Chủ Nhật
        LocalDate endDate = startDate.plusDays(6); // Thứ 2 + 6 = CN

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        Long total = lichHocRepository.countDistinctEventInWeek(userId, start, end);

        return new WeeklyEventCountDTO(year, week, total);
    }

    @Override
    public long countUpcomingDeadlines() {
        Integer userId = currentUserService.getCurrentUserId();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoWeeksLater = now.plusWeeks(2);

        return lichHocRepository.countUpcomingDeadlines(
                userId,
                now,
                twoWeeksLater
        );
    }

    @Override
    public TopSubjectDTO getTopSubjectInMonth(Integer year, Integer month) {

        Integer userId = currentUserService.getCurrentUserId();

        List<TopSubjectDTO> result =
                lichHocRepository.findTopSubjectInMonth(
                        userId,
                        month,
                        year
                );

        // Không có dữ liệu
        if (result.isEmpty()) {
            return null;
        }

        // Vì đã ORDER BY DESC → lấy phần tử đầu
        return result.get(0);
    }
    @Override
    public Map<String, List<TodayEventDTO>> getTodayEvents() {

        Integer userId = currentUserService.getCurrentUserId();

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        List<LichHoc> events =
                lichHocRepository.findTodayEvents(userId, start, end);

        return events.stream()
                .map(this::mapToTodayDTO)
                .collect(Collectors.groupingBy(TodayEventDTO::getLoaiSuKien));
    }

    private TodayEventDTO mapToTodayDTO(LichHoc l) {

        String tenMonHoc = null;
        if (l.getMaMonHoc() != null) {
            tenMonHoc = monHocCaNhanRepository
                    .findById(l.getMaMonHoc())
                    .map(MonHocCaNhan::getTenMonHoc)
                    .orElse(null);
        }

        TodayEventDTO.TodayEventDTOBuilder builder =
                TodayEventDTO.builder()
                        .maSuKien(l.getMaSuKien())
                        .loaiSuKien(l.getLoaiSuKien())
                        .tieuDe(l.getTieuDe())
                        .tenMonHoc(tenMonHoc);

        switch (l.getLoaiSuKien()) {

            case "deadline" -> builder
                    .thoiGian(l.getThoiGianKetThuc());

            case "hoc" -> builder
                    .thoiGianHoc(
                            l.getThoiGianBatDau().toLocalTime()
                                    + " - " +
                                    l.getThoiGianKetThuc().toLocalTime()
                    )
                    .diaDiem(l.getDiaDiem());

            case "thi" -> builder
                    .thoiGian(l.getThoiGianBatDau())
                    .diaDiem(l.getDiaDiem());

            case "on_tap" -> builder
                    .thoiGian(l.getThoiGianBatDau())
                    .moTa(l.getMoTa());
        }

        return builder.build();
    }

    @Override
    public ImportSchedulePreviewDTO importFromImage(MultipartFile image) {

        validateImageFile(image);
        // ========== 1. OCR ==========
        String ocrText;
        try {
            File temp = convertMultipartToImageFile(image);

            // DÙNG BEAN TESSERACT (đã set datapath + vie)
            ocrText = tesseract.doOCR(temp);

            temp.delete();

        } catch (Exception e) {
            throw new RuntimeException("OCR thất bại: " + e.getMessage(), e);
        }

        if (ocrText == null || ocrText.isBlank()) {
            throw new RuntimeException("Không đọc được nội dung từ ảnh");
        }

        // ========== 2. PROMPT CHO GEMINI ==========
        String prompt = """
                String prompt = ""\"
                Đây là nội dung OCR từ ảnh thời khóa biểu sinh viên:
                
                %s
                
                Quy ước tiết học:
                - Tiết 1: 07:00 – 07:50
                - Tiết 2: 07:50 – 08:40
                - Tiết 3: 09:00 – 09:50
                - Tiết 4: 09:50 – 10:40
                - Tiết 5: 10:40 – 11:30
                - Tiết 6: 13:00 – 13:50
                - Tiết 7: 13:50 – 14:40
                - Tiết 8: 15:00 – 15:40
                - Tiết 9: 15:40 – 16:30
                - Tiết 10: 16:40 – 17:30
                
                Nếu môn học ghi "tiết X – Y" thì:
                - gioBatDau = giờ bắt đầu của tiết X
                - gioKetThuc = giờ kết thúc của tiết Y
                
                Hãy trích xuất thành JSON theo format SAU, KHÔNG THÊM TEXT KHÁC:
                
                {
                  "subjects": [
                    {
                      "tenMonHoc": "Tên môn học",
                      "lich": [
                        {
                          "thu": "Thứ 2",
                          "gioBatDau": "07:00",
                          "gioKetThuc": "09:40",
                          "diaDiem": "P101"
                        }
                      ]
                    }
                  ]
                }
                
                YÊU CẦU:
                - gioBatDau và gioKetThuc phải ở dạng HH:mm
                - Nếu không rõ tiết học → bỏ qua dòng đó
                - CHỈ TRẢ JSON THUẦN
                - KHÔNG markdown, KHÔNG ```json
                ""\".formatted(ocrText);
                
        """.formatted(ocrText);

        try {
            // ========== 3. GỌI GEMINI ==========
            String rawResponse = geminiService.askGemini(prompt);

            // 🔥 FIX LỖI BACKTICK
            String cleanJson = cleanGeminiJson(rawResponse);

            GeminiImportScheduleDTO geminiDTO =
                    objectMapper.readValue(cleanJson, GeminiImportScheduleDTO.class);

            // ========== 4. BUILD PREVIEW ==========
            Integer userId = currentUserService.getCurrentUserId();

            List<ImportMonHocPreviewDTO> monHocMoi = new ArrayList<>();
            List<ImportLichHocPreviewDTO> lichHoc = new ArrayList<>();

            for (var subject : geminiDTO.getSubjects()) {

                boolean exists =
                        monHocCaNhanRepository
                                .existsByMaNguoiDungAndTenMonHoc(
                                        userId,
                                        subject.getTenMonHoc()
                                );

                if (!exists) {
                    monHocMoi.add(
                            ImportMonHocPreviewDTO.builder()
                                    .tenMonHoc(subject.getTenMonHoc())
                                    .build()
                    );
                }

                for (var lich : subject.getLich()) {
                    lichHoc.add(
                            ImportLichHocPreviewDTO.builder()
                                    .tenMonHoc(subject.getTenMonHoc())
                                    .thu(lich.getThu())
                                    .gioBatDau(lich.getGioBatDau())
                                    .gioKetThuc(lich.getGioKetThuc())
                                    .diaDiem(lich.getDiaDiem())
                                    .build()
                    );
                }
            }

            return ImportSchedulePreviewDTO.builder()
                    .monHocMoi(monHocMoi)
                    .lichHoc(lichHoc)
                    .build();

        } catch (com.google.genai.errors.ServerException e) {

            // ✅ GEMINI QUÁ TẢI
            throw new RuntimeException(
                    "AI đang quá tải, vui lòng thử lại sau 1–2 phút",
                    e
            );

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {

            // ✅ GEMINI TRẢ TEXT KHÔNG PHẢI JSON
            throw new RuntimeException(
                    "AI trả dữ liệu không đúng định dạng JSON",
                    e
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Lỗi không xác định khi xử lý dữ liệu từ AI",
                    e
            );
        }

    }


    // ================= CONFIRM =================

    @Override
    @Transactional
    public void confirmImport(ImportScheduleConfirmRequest request) {

        Integer userId = currentUserService.getCurrentUserId();

        String emailMacDinh = nguoiDungRepository.findById(userId)
                .map(NguoiDung::getEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy email người dùng"));

        Map<String, Integer> monHocMap = new HashMap<>();

        for (var item : request.getLichHoc()) {

            Integer maMonHoc = monHocMap.computeIfAbsent(
                    item.getTenMonHoc(),
                    ten -> monHocCaNhanRepository.findByMaNguoiDung(userId).stream()
                            .filter(m -> m.getTenMonHoc().equalsIgnoreCase(ten))
                            .findFirst()
                            .map(MonHocCaNhan::getMaMonHoc)
                            .orElseGet(() -> {
                                MonHocCaNhan mh = monHocCaNhanRepository.save(
                                        MonHocCaNhan.builder()
                                                .maNguoiDung(userId)
                                                .tenMonHoc(ten)
                                                .build()
                                );
                                return mh.getMaMonHoc();
                            })
            );

            LocalDate date = ThuUtils.nextDateFromThu(item.getThu());
            LocalTime start = LocalTime.parse(item.getGioBatDau());
            LocalTime end = LocalTime.parse(item.getGioKetThuc());

            LichHoc savedEvent = lichHocRepository.saveAndFlush(
                    LichHoc.builder()
                            .maNguoiDung(userId)
                            .maMonHoc(maMonHoc)
                            .tieuDe("Học " + item.getTenMonHoc())
                            .loaiSuKien("hoc")
                            .thoiGianBatDau(LocalDateTime.of(date, start))
                            .thoiGianKetThuc(LocalDateTime.of(date, end))
                            .diaDiem(item.getDiaDiem())
                            .moTa(item.getMoTa())
                            .mucDoUuTien("binh_thuong")
                            .build()
            );

            // ===== TẠO NHẮC NHỞ GIỐNG createEvent =====

            if (Boolean.TRUE.equals(item.getNhacTruocBatDau())) {
                nhacNhoRepo.save(buildReminder(
                        savedEvent.getMaSuKien(),
                        userId,
                        false,
                        calcReminderTime(
                                savedEvent.getThoiGianBatDau(),
                                item.getSoPhutTruocBatDau()
                        ),
                        emailMacDinh
                ));
            }

            if (Boolean.TRUE.equals(item.getNhacTruocKetThuc())) {
                nhacNhoRepo.save(buildReminder(
                        savedEvent.getMaSuKien(),
                        userId,
                        true,
                        calcReminderTime(
                                savedEvent.getThoiGianKetThuc(),
                                item.getSoPhutTruocKetThuc()
                        ),
                        emailMacDinh
                ));
            }

            // ===== XỬ LÝ LẶP LỊCH (nếu có) =====

            if (Boolean.TRUE.equals(item.getLapLai())
                    && item.getRepeatRule() != null) {

                handleRepeatImport(
                        savedEvent,
                        item.getRepeatRule(),
                        userId,
                        emailMacDinh,
                        item.getNhacTruocBatDau(),
                        item.getSoPhutTruocBatDau(),
                        item.getNhacTruocKetThuc(),
                        item.getSoPhutTruocKetThuc()
                );

            }
        }
    }

    private void handleRepeatImport(
            LichHoc base,
            RepeatRuleDTO rule,
            Integer userId,
            String emailMacDinh,
            Boolean nhacTruocBatDau,
            Integer soPhutTruocBatDau,
            Boolean nhacTruocKetThuc,
            Integer soPhutTruocKetThuc
    )
    {
        int created = 0;

        LocalDateTime nextStart = base.getThoiGianBatDau();
        LocalDateTime nextEnd = base.getThoiGianKetThuc();

        while (true) {

            if (rule.getCount() != null && created >= rule.getCount()) {
                break;
            }

            if (rule.getUntil() != null) {
                LocalDate until = LocalDate.parse(rule.getUntil());
                if (nextStart.toLocalDate().isAfter(until)) {
                    break;
                }
            }

            switch (rule.getFreq()) {
                case "DAILY" -> {
                    nextStart = nextStart.plusDays(rule.getInterval());
                    nextEnd = nextEnd.plusDays(rule.getInterval());
                }
                case "WEEKLY" -> {
                    nextStart = nextStart.plusWeeks(rule.getInterval());
                    nextEnd = nextEnd.plusWeeks(rule.getInterval());
                }
                case "MONTHLY" -> {
                    nextStart = nextStart.plusMonths(rule.getInterval());
                    nextEnd = nextEnd.plusMonths(rule.getInterval());
                }
            }

            LichHoc cloned = lichHocRepository.saveAndFlush(
                    LichHoc.builder()
                            .maNguoiDung(userId)
                            .maMonHoc(base.getMaMonHoc())
                            .tieuDe(base.getTieuDe())
                            .loaiSuKien(base.getLoaiSuKien())
                            .diaDiem(base.getDiaDiem())
                            .moTa(base.getMoTa())
                            .mucDoUuTien(base.getMucDoUuTien())
                            .thoiGianBatDau(nextStart)
                            .thoiGianKetThuc(nextEnd)
                            .build()
            );

            created++;

// ===== COPY REMINDER CHO BẢN LẶP =====

// Nhắc trước bắt đầu
            if (Boolean.TRUE.equals(nhacTruocBatDau)) {
                nhacNhoRepo.save(buildReminder(
                        cloned.getMaSuKien(),
                        userId,
                        false,
                        calcReminderTime(nextStart, soPhutTruocBatDau),
                        emailMacDinh
                ));
            }

// 🔥 QUAN TRỌNG: BỔ SUNG NHẮC TRƯỚC KẾT THÚC
            if (Boolean.TRUE.equals(nhacTruocKetThuc)) {
                nhacNhoRepo.save(buildReminder(
                        cloned.getMaSuKien(),
                        userId,
                        true,
                        calcReminderTime(nextEnd, soPhutTruocKetThuc),
                        emailMacDinh
                ));
            }

        }
    }

    private String cleanGeminiJson(String raw) {
        if (raw == null) {
            throw new RuntimeException("Gemini trả về null");
        }

        return raw
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }

    private void validateImageFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File ảnh không được rỗng");
        }

        String contentType = file.getContentType();

        List<String> allowed = List.of(
                "image/png",
                "image/jpeg",
                "image/jpg",
                "image/bmp",
                "image/gif",
                "image/webp",
                "image/tiff"
        );

        if (contentType == null || !allowed.contains(contentType.toLowerCase())) {
            throw new RuntimeException(
                    "Chỉ hỗ trợ các định dạng ảnh: PNG, JPG, JPEG, BMP, GIF, WEBP, TIFF"
            );
        }
    }

    private File convertMultipartToImageFile(MultipartFile multipartFile) throws Exception {

        // Lấy đuôi file gốc
        String originalName = multipartFile.getOriginalFilename();

        String extension = "png"; // mặc định

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        }

        // Tạo file tạm theo đúng định dạng gốc
        File tempFile = File.createTempFile("ocr-", "." + extension);

        multipartFile.transferTo(tempFile);

        // Một số định dạng OCR kém → convert về PNG chuẩn
        String extLower = extension.toLowerCase();

        List<String> needConvert = List.of("webp", "tiff", "gif");

        if (needConvert.contains(extLower)) {

            // Convert sang PNG để OCR ổn định hơn
            File pngFile = File.createTempFile("ocr-converted-", ".png");

            java.awt.image.BufferedImage image =
                    javax.imageio.ImageIO.read(tempFile);

            javax.imageio.ImageIO.write(image, "png", pngFile);

            tempFile.delete();

            return pngFile;
        }

        return tempFile;
    }
    @Override
    public List<LichHocUpcomingDTO> getUpcomingEvents() {

        Integer userId = currentUserService.getCurrentUserId();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusDays(7);

        List<LichHoc> events =
                lichHocRepository.findTop4ByMaNguoiDungAndThoiGianKetThucBetweenOrderByThoiGianKetThucAsc(
                        userId,
                        now,
                        nextWeek
                );

        return events.stream()
                .map(e -> LichHocUpcomingDTO.builder()
                        .tieuDe(e.getTieuDe())
                        .diaDiem(e.getDiaDiem())
                        .moTa(e.getMoTa())
                        .mucDoUuTien(e.getMucDoUuTien())
                        .thoiGianKetThuc(
                                LichHocTimeFormatter.format(e.getThoiGianKetThuc())
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }
}
