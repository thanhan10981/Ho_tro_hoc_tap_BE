package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.BoFlashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoFlashcardRepository extends JpaRepository<BoFlashcard, Integer> {
    @Query("""
    SELECT b.maBoFlashcard, b.tenBo, b.moTa, COUNT(f.maFlashcard)
    FROM BoFlashcard b
    LEFT JOIN Flashcard f ON f.maBoFlashcard = b.maBoFlashcard
    WHERE b.maNguoiDung = :userId
    GROUP BY b.maBoFlashcard, b.tenBo, b.moTa
    """)
        List<Object[]> findFlashcardSetsWithCount(@Param("userId") Integer userId);


    List<BoFlashcard> findByMaNguoiDung(Integer maNguoiDung);
}
