package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.CuocTroChuyenAI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuocTroChuyenRepository
        extends JpaRepository<CuocTroChuyenAI, Integer> {
    List<CuocTroChuyenAI>
    findByUserIdOrderByCreatedAtDesc(Integer userId);
}
