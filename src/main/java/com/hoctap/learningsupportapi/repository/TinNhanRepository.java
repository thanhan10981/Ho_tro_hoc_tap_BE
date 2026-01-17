package com.hoctap.learningsupportapi.repository;
import com.hoctap.learningsupportapi.model.entity.TinNhanAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TinNhanRepository
        extends JpaRepository<TinNhanAI, Integer> {

    List<TinNhanAI> findByConversation_IdOrderByCreatedAtAsc(Integer id);
    @Modifying
    @Query("DELETE FROM TinNhanAI t WHERE t.id = :id")
    void deleteByMaCuocTroChuyen(@Param("id") Integer id);

}
