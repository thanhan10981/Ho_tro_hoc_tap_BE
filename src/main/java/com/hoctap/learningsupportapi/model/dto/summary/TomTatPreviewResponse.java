package com.hoctap.learningsupportapi.model.dto.summary;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TomTatPreviewResponse {

    private String tieuDe;
    private String noiDungTomTat;
    private Integer soTu;
    private Integer soTrang;
}
