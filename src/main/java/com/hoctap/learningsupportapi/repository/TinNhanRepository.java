package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.TinNhanAI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TinNhanRepository
        extends JpaRepository<TinNhanAI, Integer> {

    List<TinNhanAI> findByConversation_IdOrderByCreatedAtAsc(Integer id);
}
