package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.DanhGiaTaiLieuChung;
import com.hoctap.learningsupportapi.model.entity.DanhGiaTaiLieuChungId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DanhGiaTaiLieuChungRepository
        extends JpaRepository<DanhGiaTaiLieuChung, DanhGiaTaiLieuChungId> {

    @Query("""
        select avg(d.soSao)
        from DanhGiaTaiLieuChung d
        where d.taiLieu.id = :docId
    """)
    Double avgRating(@Param("docId") Integer docId);

    long countByTaiLieu_Id(Integer docId);
}
