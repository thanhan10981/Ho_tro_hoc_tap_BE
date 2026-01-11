package com.hoctap.learningsupportapi.service.lichhoc;


import com.hoctap.learningsupportapi.model.dto.lichhoc.MonHocCreateDTO;
import com.hoctap.learningsupportapi.model.dto.lichhoc.MonHocResponseDTO;

import java.util.List;

public interface MonHocCaNhanService {

    MonHocResponseDTO createMonHoc(MonHocCreateDTO dto);

    List<MonHocResponseDTO> getMyMonHoc();
}
