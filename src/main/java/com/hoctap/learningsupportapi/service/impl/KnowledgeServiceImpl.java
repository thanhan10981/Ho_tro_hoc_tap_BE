package com.hoctap.learningsupportapi.service.impl;

import com.hoctap.learningsupportapi.exception.BadRequestException;
import com.hoctap.learningsupportapi.mapper.TaiLieuChungMapper;
import com.hoctap.learningsupportapi.mapper.TaiLieuNhanMapper;
import com.hoctap.learningsupportapi.model.dto.KnowledgeDocResponse;
import com.hoctap.learningsupportapi.model.dto.KnowledgeSearchRequest;
import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import com.hoctap.learningsupportapi.model.entity.TaiLieuNhan;
import com.hoctap.learningsupportapi.model.entity.TaiLieuNhanId;
import com.hoctap.learningsupportapi.repository.NguoiDungRepository;
import com.hoctap.learningsupportapi.repository.NhanRepository;
import com.hoctap.learningsupportapi.repository.TaiLieuChungRepository;
import com.hoctap.learningsupportapi.repository.TaiLieuNhanRepository;
import com.hoctap.learningsupportapi.service.KnowledgeService;



import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KnowledgeServiceImpl implements KnowledgeService {

    private final TaiLieuChungRepository taiLieuRepo;
    private final TaiLieuNhanRepository taiLieuNhanRepo;
    private final NhanRepository nhanRepo;
    private final NguoiDungRepository nguoiDungRepo;
    private final TaiLieuChungMapper chungMapper;
    private final TaiLieuNhanMapper nhanMapper;

    @Override
    public Page<KnowledgeDocResponse> getCommonDocs(int page, int size) {
        return taiLieuRepo
                .findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(chungMapper::toDto);
    }

    @Override
    public Page<KnowledgeDocResponse> searchFull(KnowledgeSearchRequest req) {
        Pageable pageable = PageRequest.of(
                req.getPage(),
                req.getSize(),
                Sort.by(req.getSort()).descending()
        );

        return taiLieuRepo.searchFull(
                req.getKeyword(),
                req.getType(),
                req.getSubject(),
                req.getRating(),
                pageable
        ).map(chungMapper::toDto);
    }

    @Override
    public void saveToPersonal(Integer userId, Integer docId) {

        // giả sử nhãn mặc định = 1
        Integer defaultNhanId = 1;

        TaiLieuNhanId id = new TaiLieuNhanId(
                docId,
                userId,
                defaultNhanId
        );

        if (taiLieuNhanRepo.existsById(id)) {
            throw new BadRequestException("Tài liệu đã tồn tại trong kho cá nhân");
        }

        TaiLieuNhan tln = new TaiLieuNhan();
        tln.setId(id);
        tln.setTaiLieu(taiLieuRepo.getReferenceById(docId));
        tln.setNguoiDung(nguoiDungRepo.getReferenceById(userId));
        tln.setNhan(nhanRepo.getReferenceById(defaultNhanId));
        tln.setStatus("TODO");
        tln.setSavedAt(LocalDateTime.now());

        taiLieuNhanRepo.save(tln);
    }



    @Override
    public List<PersonalDocResponse> getPersonalDocs(Integer userId) {
        return taiLieuNhanRepo.findByNguoiDung_Id(userId)
                .stream()
                .map(nhanMapper::toDto)
                .toList();
    }

}
