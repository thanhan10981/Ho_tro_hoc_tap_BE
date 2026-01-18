package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.TaiLieuChung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaiLieuChungRepository extends JpaRepository<TaiLieuChung, Integer> {

    /**
     * SEARCH FULL – có filter + rating
     * LƯU Ý:
     * - JOIN qua field mapping (t.danhGias)
     * - KHÔNG join bằng tên entity
     */
    @Query("""
    SELECT t
    FROM TaiLieuChung t
    LEFT JOIN t.danhGias d
    WHERE (:keyword IS NULL OR t.title LIKE %:keyword%)
      AND (:type IS NULL OR t.type = :type)
      AND (:linhVucId IS NULL OR t.linhVuc.id = :linhVucId)
      AND (:chuDeId IS NULL OR t.chuDe.id = :chuDeId)
      AND (:capBacId IS NULL OR t.capBac.id = :capBacId)
    GROUP BY t
    HAVING (:rating IS NULL OR AVG(d.soSao) >= :rating)
""")
    Page<TaiLieuChung> searchFull(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("linhVucId") Integer linhVucId,
            @Param("chuDeId") Integer chuDeId,
            @Param("capBacId") Integer capBacId,
            @Param("rating") Integer rating,
            Pageable pageable
    );

}
