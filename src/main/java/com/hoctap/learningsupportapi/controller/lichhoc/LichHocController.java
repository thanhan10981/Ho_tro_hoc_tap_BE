package com.hoctap.learningsupportapi.controller.lichhoc;

import com.hoctap.learningsupportapi.model.dto.lichhoc.*;
import com.hoctap.learningsupportapi.service.lichhoc.EmailOtpService;
import com.hoctap.learningsupportapi.service.lichhoc.LichHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lich-hoc")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LichHocController {

    private final LichHocService lichHocService;
    private final EmailOtpService emailOtpService;

    // ===================== GET CALENDAR =====================

    @GetMapping("/calendar")
    public List<LichHocCalendarDTO> getCalendarEvents(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return lichHocService.getLichHocTrongKhoang(fromDate, toDate);
    }

    // ===================== CREATE EVENT =====================

    @PostMapping
    public ResponseEntity<Void> createEvent(
            @RequestBody CreateEventRequest request
    ) {
        lichHocService.createEvent(request);
        return ResponseEntity.ok().build();
    }

    // ===================== UPDATE EVENT =====================

    @PutMapping("/{maSuKien}")
    public ResponseEntity<String> updateEvent(
            @PathVariable Integer maSuKien,
            @RequestBody UpdateLichHocDTO dto
    ) {
        lichHocService.updateLichHoc(maSuKien, dto);
        return ResponseEntity.ok("Cập nhật sự kiện thành công");
    }

    // ===================== UPDATE REMINDER =====================
    // loai = false (nhắc bắt đầu) | true (nhắc kết thúc)

    @PutMapping("/{maSuKien}/nhac-nho/{loai}")
    public ResponseEntity<String> updateNhacNho(
            @PathVariable Integer maSuKien,
            @PathVariable Boolean loai,
            @RequestBody UpdateNhacNhoDTO dto
    ) {
        lichHocService.updateNhacNho(maSuKien, loai, dto);
        return ResponseEntity.ok("Cập nhật nhắc nhở thành công");
    }

    // ===================== DELETE REMINDER =====================

    @DeleteMapping("/{maSuKien}/nhac-nho/{loai}")
    public ResponseEntity<String> deleteNhacNho(
            @PathVariable Integer maSuKien,
            @PathVariable Boolean loai
    ) {
        lichHocService.deleteNhacNho(maSuKien, loai);
        return ResponseEntity.ok("Xóa nhắc nhở thành công");
    }

    // ===================== EMAIL REMINDER =====================

    @PutMapping("/email-nhac-nho")
    public ResponseEntity<String> updateReminderEmail(
            @RequestBody UpdateReminderEmailRequest request
    ) {
        boolean valid = emailOtpService.verify(
                request.getEmailMoi(),
                request.getOtp()
        );

        if (!valid) {
            return ResponseEntity
                    .badRequest()
                    .body("OTP không đúng hoặc đã hết hạn");
        }

        lichHocService.updateReminderEmailForUser(
                request.getEmailMoi()
        );

        return ResponseEntity.ok("Cập nhật email nhắc nhở thành công");
    }


    @GetMapping("/email-nhac-nho")
    public ResponseEntity<String> getReminderEmail() {
        return ResponseEntity.ok(
                lichHocService.getReminderEmailForUser()
        );
    }

    // ===================== DELETE EVENT =====================

    @DeleteMapping("/{maSuKien}")
    public ResponseEntity<String> deleteEvent(
            @PathVariable Integer maSuKien
    ) {
        lichHocService.deleteLichHoc(maSuKien);
        return ResponseEntity.ok("Xóa sự kiện thành công");
    }

    @PostMapping("/search")
    public List<LichHocCalendarDTO> search(@RequestBody LichHocSearchRequest request) {
        return lichHocService.searchLichHoc(request);
    }

    @GetMapping("/weekly")
    public WeeklyEventCountDTO countWeeklyEvents(
            @RequestParam int year,
            @RequestParam int week
    ) {
        return lichHocService.countWeeklyEvents(year, week);
    }

    @GetMapping("/upcoming-deadlines")
    public Map<String, Long> getUpcomingDeadlineCount() {
        return Map.of(
                "total",
                lichHocService.countUpcomingDeadlines()
        );
    }

    @GetMapping("/top-subject")
    public ResponseEntity<TopSubjectDTO> getTopSubjectInMonth(
            @RequestParam int year,
            @RequestParam int month
    ) {
        TopSubjectDTO dto =
                lichHocService.getTopSubjectInMonth(year, month);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/today")
    public ResponseEntity<Map<String, List<TodayEventDTO>>> getTodayEvents() {
        return ResponseEntity.ok(
                lichHocService.getTodayEvents()
        );
    }

    @PostMapping("/import-image")
    public ResponseEntity<ImportSchedulePreviewDTO> importFromImage(
            @RequestParam MultipartFile image
    ) {
        return ResponseEntity.ok(lichHocService.importFromImage(image));
    }

    @PostMapping("/import-image/confirm")
    public ResponseEntity<Void> confirmImport(
            @RequestBody ImportScheduleConfirmRequest request
    ) {
        lichHocService.confirmImport(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/upcoming")
    public List<LichHocUpcomingDTO> getUpcomingEvents() {
        return lichHocService.getUpcomingEvents();
    }
}
