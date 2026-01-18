package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.CapBac;
import com.hoctap.learningsupportapi.model.entity.ChuDe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChuDeRepository extends JpaRepository<ChuDe, Integer> {
    List<ChuDe> findByLinhVuc_Id(Integer linhVucId);
}

