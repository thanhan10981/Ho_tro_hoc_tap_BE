package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.dto.UpcomingEventDto;
import com.hoctap.learningsupportapi.model.entity.LichHoc;
import com.hoctap.learningsupportapi.model.enums.LoaiSuKien;
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
    @Query("""
 SELECT l FROM LichHoc l
 WHERE l.maNguoiDung = :userId
 AND l.loaiSuKien = :loai
 AND l.thoiGianBatDau BETWEEN :start AND :end
""")
    List<LichHoc> findSuKienTrongTuan(
            Integer userId,
            LoaiSuKien loai,
            LocalDateTime start,
            LocalDateTime end
    );


    @Query(value = """
    SELECT TOP 5
        lh.ma_su_kien AS maSuKien,
        lh.tieu_de AS tieuDe,
        lh.mo_ta AS moTa,
        lh.thoi_gian_bat_dau AS thoiGianBatDau,
        lh.muc_do_uu_tien AS mucDoUuTien,
        lh.dia_diem AS diaDiem
    FROM dbo.lich_hoc lh
    WHERE lh.ma_nguoi_dung = :maNguoiDung
      AND lh.thoi_gian_bat_dau >= GETDATE()
    ORDER BY
      CASE lh.muc_do_uu_tien
        WHEN 'quan_trong' THEN 1
        WHEN 'binh_thuong' THEN 2
        ELSE 3
      END,
      lh.thoi_gian_bat_dau
""", nativeQuery = true)
    List<UpcomingEventDto> findUpcomingEvents(
            @Param("maNguoiDung") Integer maNguoiDung
    );



    @Query("""
             SELECT l FROM LichHoc l
             WHERE l.maNguoiDung = :userId
             AND l.thoiGianBatDau BETWEEN :start AND :end
            """)
    List<LichHoc> findAllTrongTuan(
            Integer userId,
            LocalDateTime start,
            LocalDateTime end
    );

}
