package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.TomTatTuKhoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TomTatTuKhoaRepository
        extends JpaRepository<TomTatTuKhoa, Integer> {

    List<TomTatTuKhoa> findByMaTomTat(Integer maTomTat);

    void deleteByMaTomTat(Integer maTomTat);
}
