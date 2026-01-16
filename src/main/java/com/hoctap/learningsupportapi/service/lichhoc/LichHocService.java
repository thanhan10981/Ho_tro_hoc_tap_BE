package com.hoctap.learningsupportapi.service.lichhoc;


import com.hoctap.learningsupportapi.model.dto.lichhoc.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface LichHocService {

    List<LichHocCalendarDTO> getLichHocCuaNguoiDungHienTai();

    List<LichHocCalendarDTO> getLichHocTrongKhoang(String fromDate, String toDate);

    void createEvent(CreateEventRequest request);

    void updateLichHoc(Integer maSuKien, UpdateLichHocDTO dto);

    void updateNhacNho(
            Integer maSuKien,
            Boolean loaiNhacNho,
            UpdateNhacNhoDTO dto
    );

    void deleteNhacNho(
            Integer maSuKien,
            Boolean loaiNhacNho
    );
    void updateReminderEmailForUser(String emailMoi);

    String getReminderEmailForUser();
    void deleteLichHoc(Integer maSuKien);

    List<LichHocCalendarDTO> searchLichHoc(LichHocSearchRequest request);
    WeeklyEventCountDTO countWeeklyEvents(Integer year, Integer week);
    long countUpcomingDeadlines();
    TopSubjectDTO getTopSubjectInMonth(Integer year, Integer month);
    Map<String, List<TodayEventDTO>> getTodayEvents();

    ImportSchedulePreviewDTO importFromImage(MultipartFile image);

    void confirmImport(ImportScheduleConfirmRequest request);

    List<LichHocUpcomingDTO> getUpcomingEvents();
}

