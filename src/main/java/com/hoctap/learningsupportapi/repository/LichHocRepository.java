package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.dto.lichhoc.TopSubjectDTO;
import com.hoctap.learningsupportapi.model.entity.LichHoc;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    Optional<LichHoc> findByMaSuKienAndMaNguoiDung(
            Integer maSuKien,
            Integer maNguoiDung
    );

    @Query("""
    SELECT l
    FROM LichHoc l
    WHERE l.maNguoiDung = :userId
      AND (:keyword IS NULL 
           OR LOWER(l.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:maMonHoc IS NULL 
           OR l.maMonHoc = :maMonHoc)
      AND (:loaiSuKien IS NULL 
           OR l.loaiSuKien IN :loaiSuKien)
    ORDER BY l.thoiGianBatDau DESC
""")
    List<LichHoc> search(
            @Param("userId") Integer userId,
            @Param("keyword") String keyword,
            @Param("maMonHoc") Integer maMonHoc,
            @Param("loaiSuKien") List<String> loaiSuKien   // ✅ LIST
    );

    @Query("""
        SELECT COUNT(DISTINCT l.maSuKien)
        FROM LichHoc l
        WHERE l.maNguoiDung = :userId
          AND l.thoiGianBatDau BETWEEN :start AND :end
    """)
    Long countDistinctEventInWeek(
            @Param("userId") Integer userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT COUNT(l)
        FROM LichHoc l
        WHERE l.maNguoiDung = :userId
          AND l.loaiSuKien = 'deadline'
          AND l.thoiGianBatDau BETWEEN :from AND :to
    """)
    long countUpcomingDeadlines(
            @Param("userId") Integer userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
    SELECT new com.hoctap.learningsupportapi.model.dto.lichhoc.TopSubjectDTO(
        mh.maMonHoc,
        mh.tenMonHoc,
        COUNT(lh.maSuKien)
    )
    FROM LichHoc lh
    JOIN MonHocCaNhan mh
        ON lh.maMonHoc = mh.maMonHoc
    WHERE lh.maNguoiDung = :userId
      AND FUNCTION('MONTH', lh.thoiGianBatDau) = :month
      AND FUNCTION('YEAR', lh.thoiGianBatDau) = :year
    GROUP BY mh.maMonHoc, mh.tenMonHoc
    ORDER BY COUNT(lh.maSuKien) DESC
    """)
    List<TopSubjectDTO> findTopSubjectInMonth(
            @Param("userId") Integer userId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT l
        FROM LichHoc l
        WHERE l.maNguoiDung = :userId
          AND l.thoiGianBatDau BETWEEN :start AND :end
        ORDER BY l.thoiGianBatDau ASC
    """)
    List<LichHoc> findTodayEvents(
            Integer userId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<LichHoc> findTop4ByMaNguoiDungAndThoiGianKetThucBetweenOrderByThoiGianKetThucAsc(
            Integer maNguoiDung,
            LocalDateTime start,
            LocalDateTime end
    );
}
