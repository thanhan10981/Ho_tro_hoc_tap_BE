package com.hoctap.learningsupportapi.model.dto.summary;



import lombok.Data;

@Data
public class TomTatPreviewRequest {

    private String tieuDe;
    private String noiDung; // <= 5000 chữ
    private String doDai;   // NGAN | VUA | DAI
    private Integer maMonHoc;

    private boolean highlightTuKhoa;
    private boolean themViDu;
    private boolean taoCauHoiOnTap;

    private boolean autoGenerateTitle;
}
