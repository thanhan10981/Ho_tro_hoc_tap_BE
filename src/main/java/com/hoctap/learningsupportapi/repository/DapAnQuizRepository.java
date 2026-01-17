package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.DapAnQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DapAnQuizRepository
        extends JpaRepository<DapAnQuiz, Integer> {
}
