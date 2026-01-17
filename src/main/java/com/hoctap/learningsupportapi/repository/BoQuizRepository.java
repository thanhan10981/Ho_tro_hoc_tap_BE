package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.BoQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoQuizRepository extends JpaRepository<BoQuiz, Integer> {
    List<BoQuiz> findByMaNguoiDung(Integer maNguoiDung);
}
