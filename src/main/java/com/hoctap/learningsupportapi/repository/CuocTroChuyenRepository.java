package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.CuocTroChuyenAI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuocTroChuyenRepository
        extends JpaRepository<CuocTroChuyenAI, Integer> {
}
