package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository
        extends JpaRepository<Flashcard, Integer> {

    List<Flashcard> findByMaBoFlashcard(Integer maBoFlashcard);
}
