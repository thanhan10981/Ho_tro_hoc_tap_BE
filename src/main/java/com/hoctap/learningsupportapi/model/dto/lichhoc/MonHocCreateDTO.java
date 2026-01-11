package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Data;

@Data
public class MonHocCreateDTO {

    private String tenMonHoc;
    private String moTa;
    private String mucDoHoc; // moi_bat_dau | dang_hoc | on_tap | thanh_thao
}
