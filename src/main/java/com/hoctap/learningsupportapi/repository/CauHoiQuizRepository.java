package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.CauHoiQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CauHoiQuizRepository
        extends JpaRepository<CauHoiQuiz, Integer> {

    List<CauHoiQuiz> findByMaQuiz(Integer maQuiz);
}
