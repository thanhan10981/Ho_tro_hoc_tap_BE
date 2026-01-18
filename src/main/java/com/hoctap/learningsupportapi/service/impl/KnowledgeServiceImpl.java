package com.hoctap.learningsupportapi.service.impl;

import com.hoctap.learningsupportapi.exception.BadRequestException;
import com.hoctap.learningsupportapi.exception.ResourceNotFoundException;
import com.hoctap.learningsupportapi.mapper.LuuTaiLieuMapper;
import com.hoctap.learningsupportapi.mapper.TaiLieuChungMapper;
import com.hoctap.learningsupportapi.mapper.TaiLieuNhanMapper;
import com.hoctap.learningsupportapi.model.dto.KnowledgeDocDetailResponse;
import com.hoctap.learningsupportapi.model.dto.SidebarStatResponse;
import com.hoctap.learningsupportapi.model.dto.KnowledgeDocResponse;
import com.hoctap.learningsupportapi.model.dto.KnowledgeSearchRequest;
import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import com.hoctap.learningsupportapi.model.entity.*;
import com.hoctap.learningsupportapi.repository.*;
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
import java.util.stream.Collectors;

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
    private final LuuTaiLieuRepository luuTaiLieuRepo;
    private final LuuTaiLieuMapper luuTaiLieuMapper;
    private final LinhVucRepository linhVucRepo;
    private final DanhGiaTaiLieuChungRepository danhGiaRepo;
    private final LuuTaiLieuRepository luuRepo;


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
                req.getLinhVucId(),
                req.getChuDeId(),
                req.getCapBacId(),
                req.getRating(),
                pageable
        ).map(chungMapper::toDto);
    }

    @Override
    public void saveToPersonal(Integer userId, Integer docId) {

        if (luuRepo.existsByNguoiDung_IdAndTaiLieu_Id(userId, docId)) {
            throw new RuntimeException("Tài liệu đã được lưu");
        }

        NguoiDung user = nguoiDungRepo.findById(userId).orElseThrow();
        TaiLieuChung doc = taiLieuRepo.findById(docId).orElseThrow();

        LuuTaiLieu save = new LuuTaiLieu();
        save.setId(new LuuTaiLieuId(docId, userId));
        save.setNguoiDung(user);
        save.setTaiLieu(doc);
        save.setSavedAt(LocalDateTime.now());
        save.setStatus("todo");

        luuRepo.save(save);
    }

    @Override
    public void removeFromPersonal(Integer userId, Integer docId) {

    }

    @Override
    public List<PersonalDocResponse> getPersonalDocs(Integer userId) {
        return luuRepo.findByNguoiDung_Id(userId)
                .stream()
                .map(PersonalDocResponse::from)
                .collect(Collectors.toList());
    }



    public KnowledgeDocDetailResponse getDocDetail(Integer docId) {

        TaiLieuChung tl = taiLieuRepo.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài liệu"));

        KnowledgeDocDetailResponse dto = new KnowledgeDocDetailResponse();
        dto.setId(tl.getId());
        dto.setTitle(tl.getTitle());
        dto.setDescription(tl.getDescription());
        dto.setFilePath(tl.getFilePath());
        dto.setType(tl.getType());
        dto.setSize(tl.getSize());
        dto.setViews(tl.getViews());
        dto.setDownloads(tl.getDownloads());
        dto.setCreatedAt(tl.getCreatedAt());

        dto.setCapBacId(tl.getCapBac().getId());
        dto.setCapBacName(tl.getCapBac().getTenCapBac());

        dto.setLinhVucId(tl.getLinhVuc().getId());
        dto.setLinhVucName(tl.getLinhVuc().getTenLinhVuc());

        dto.setChuDeId(tl.getChuDe().getId());
        dto.setChuDeName(tl.getChuDe().getTenChuDe());

        dto.setAvgRating(danhGiaRepo.avgRating(docId));
        dto.setTotalRating((int) danhGiaRepo.countByTaiLieu_Id(docId));

        return dto;
    }

    @Override
    public List<SidebarStatResponse> sidebarByLinhVuc() {
        return linhVucRepo.countByLinhVuc();
    }

    @Override
    public boolean isSaved(Integer userId, Integer docId) {
        return luuTaiLieuRepo.existsByNguoiDung_IdAndTaiLieu_Id(userId, docId);
    }

    @Override
    public Double getAvgRating(Integer docId) {
        return danhGiaRepo.avgRating(docId);
    }

    @Override
    public Integer getTotalRating(Integer docId) {
        return (int) danhGiaRepo.countByTaiLieu_Id(docId);
    }




    @Override
    public void addNhanToPersonalDoc(Integer userId, Integer docId, Integer nhanId) {

        // 1. check tài liệu đã được lưu chưa
        if (!luuTaiLieuRepo.existsByNguoiDung_IdAndTaiLieu_Id(userId, docId)) {
            throw new BadRequestException("Tài liệu chưa được lưu vào kho cá nhân");
        }

        // 2. tạo khóa chính
        TaiLieuNhanId id = new TaiLieuNhanId(docId, userId, nhanId);

        // 3. check đã gán nhãn chưa
        if (taiLieuNhanRepo.existsById(id)) {
            throw new BadRequestException("Nhãn đã được gán cho tài liệu này");
        }

        // 4. tạo entity
        TaiLieuNhan tln = new TaiLieuNhan();
        tln.setId(id);
        tln.setTaiLieu(taiLieuRepo.getReferenceById(docId));
        tln.setNguoiDung(nguoiDungRepo.getReferenceById(userId));
        tln.setNhan(nhanRepo.getReferenceById(nhanId));
        tln.setStatus("TODO");
        tln.setSavedAt(LocalDateTime.now());

        // 5. save
        taiLieuNhanRepo.save(tln);
    }

    public List<PersonalDocResponse> getDocsByNhan(Integer nhanId) {
        return taiLieuNhanRepo.findByNhan_Id(nhanId)
                .stream()
                .map(nhanMapper::toDto)
                .toList();
    }




}
