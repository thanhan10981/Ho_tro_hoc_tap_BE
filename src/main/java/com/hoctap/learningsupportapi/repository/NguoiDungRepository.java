package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {

    boolean existsByEmail(String email);
    Optional<NguoiDung> findByEmail(String email);

}
