package com.hoctap.learningsupportapi.repository;
import com.hoctap.learningsupportapi.model.entity.NhacNho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhacNhoRepository extends JpaRepository<NhacNho, Integer> {
}
