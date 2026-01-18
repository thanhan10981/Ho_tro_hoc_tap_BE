package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.dto.SidebarStatResponse;
import com.hoctap.learningsupportapi.model.entity.LinhVuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LinhVucRepository extends JpaRepository<LinhVuc, Integer> {

    @Query("""
        select new com.hoctap.learningsupportapi.model.dto.SidebarStatResponse(
            lv.id,
            lv.tenLinhVuc,
            count(tl.id)
        )
        from LinhVuc lv
        left join TaiLieuChung tl on tl.linhVuc = lv
        group by lv.id, lv.tenLinhVuc
    """)
    List<SidebarStatResponse> countByLinhVuc();
}
