package com.hoctap.learningsupportapi.model.dto.summary;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TomTatFilterRequest {

    // sort
    private TomTatSortType sortType;

    // search text (title + content)
    private String keyword;

    // filter
    private Integer maMonHoc;

    // date range
    private LocalDate fromDate;
    private LocalDate toDate;

    // range số trang
    private Integer minSoTrang;
    private Integer maxSoTrang;

    // range số từ
    private Integer minSoTu;
    private Integer maxSoTu;
}
