package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import com.hoctap.learningsupportapi.model.entity.LuuTaiLieu;
import com.hoctap.learningsupportapi.repository.LuuTaiLieuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalLibraryQueryService {

    private final LuuTaiLieuRepository repo;

    public List<PersonalDocResponse> getPersonalLibrary(Integer userId) {
        return repo.findByNguoiDung_Id(userId)
                .stream()
                .map(PersonalDocResponse::from)
                .toList();
    }
    public PersonalDocResponse getPersonalDocDetail(Integer userId, Integer docId) {

        LuuTaiLieu l = repo
                .findByNguoiDung_IdAndTaiLieu_Id(userId, docId)
                .orElseThrow(() ->
                        new RuntimeException("Không có quyền hoặc tài liệu không tồn tại")
                );

        return PersonalDocResponse.from(l);
    }
}
