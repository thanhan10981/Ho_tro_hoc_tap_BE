package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LichHocSearchRequest {

    private String keyword;     // tìm theo tiêu đề (LIKE)
    private Integer maMonHoc;   // môn học
    private List<String> loaiSuKien;  // hoc / thi / deadline
}
