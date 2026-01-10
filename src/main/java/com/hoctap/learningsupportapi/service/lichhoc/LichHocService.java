package com.hoctap.learningsupportapi.service.lichhoc;


import com.hoctap.learningsupportapi.model.dto.lichhoc.CreateEventRequest;
import com.hoctap.learningsupportapi.model.dto.lichhoc.LichHocCalendarDTO;

import java.util.List;

public interface LichHocService {

    List<LichHocCalendarDTO> getLichHocCuaNguoiDungHienTai();
    List<LichHocCalendarDTO> getLichHocTrongKhoang(String fromDate, String toDate);
    void createEvent(CreateEventRequest request);

}
