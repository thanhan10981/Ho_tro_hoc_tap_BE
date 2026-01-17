package com.hoctap.learningsupportapi.service.summary;


import com.hoctap.learningsupportapi.model.dto.summary.*;

import java.util.List;

public interface TomTatService {

    TomTatPreviewResponse preview(TomTatPreviewRequest request);

    void confirmAndSave(TomTatConfirmRequest request);

    List<TomTatViewDTO> getDanhSachTomTatDaLuu();

    List<TomTatViewDTO> filterTomTat(TomTatFilterRequest request);

    MonHocThongKeDTO getMonHocNhieuTomTatNhatTuan();

}
