package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.LichHoc;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LichHocRepository extends JpaRepository<LichHoc, Integer> {

    @Query("""
        SELECT l
        FROM LichHoc l
        WHERE l.maNguoiDung = :maNguoiDung
    """)
    List<LichHoc> findByMaNguoiDung(@Param("maNguoiDung") Integer maNguoiDung);

    @Query("""
    SELECT l FROM LichHoc l
    WHERE l.maNguoiDung = :userId
      AND l.thoiGianBatDau <= :toDate
      AND l.thoiGianKetThuc >= :fromDate
""")
    List<LichHoc> findByUserAndDateRange(
            @Param("userId") Integer userId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );



}
