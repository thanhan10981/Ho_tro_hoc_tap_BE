package com.hoctap.learningsupportapi.model.dto.summary;


import lombok.Data;

@Data
public class TomTatConfirmRequest {

    private Integer maMonHoc;
    private String tieuDe;
    private String noiDungTomTat;
    private Integer soTu;
    private Integer soTrang;
}
