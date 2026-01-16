package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.dto.summary.MonHocThongKeDTO;
import com.hoctap.learningsupportapi.model.entity.TomTatBaiHoc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TomTatBaiHocRepository extends JpaRepository<TomTatBaiHoc, Integer>,
        JpaSpecificationExecutor<TomTatBaiHoc> {

    List<TomTatBaiHoc> findByMaNguoiDungOrderByNgayTaoDesc(Integer maNguoiDung);

    @Query("""
    SELECT new com.hoctap.learningsupportapi.model.dto.summary.MonHocThongKeDTO(
        t.maMonHoc,
        m.tenMonHoc,
        COUNT(t)
    )
    FROM TomTatBaiHoc t
    JOIN MonHocCaNhan m ON t.maMonHoc = m.maMonHoc
    WHERE t.ngayTao >= :startOfWeek
      AND t.maNguoiDung = :userId
    GROUP BY t.maMonHoc, m.tenMonHoc
    ORDER BY COUNT(t) DESC
""")
    List<MonHocThongKeDTO> thongKeTomTatTheoMonTrongTuan(
            @Param("startOfWeek") LocalDateTime startOfWeek,
            @Param("userId") Integer userId
    );

}
