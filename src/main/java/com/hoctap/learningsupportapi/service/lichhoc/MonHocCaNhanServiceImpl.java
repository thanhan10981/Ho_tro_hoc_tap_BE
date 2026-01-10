package com.hoctap.learningsupportapi.service.lichhoc;


import com.hoctap.learningsupportapi.exception.BadRequestException;
import com.hoctap.learningsupportapi.model.dto.lichhoc.MonHocCreateDTO;
import com.hoctap.learningsupportapi.model.dto.lichhoc.MonHocResponseDTO;
import com.hoctap.learningsupportapi.model.entity.MonHocCaNhan;
import com.hoctap.learningsupportapi.repository.MonHocCaNhanRepository;
import com.hoctap.learningsupportapi.service.AuditLogService;
import com.hoctap.learningsupportapi.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonHocCaNhanServiceImpl implements MonHocCaNhanService {

    private final MonHocCaNhanRepository repository;
    private final CurrentUserService currentUserService;
    private final AuditLogService auditLogService;


    @Override
    public MonHocResponseDTO createMonHoc(MonHocCreateDTO dto) {
        Integer userId = currentUserService.getCurrentUserId();
        if (repository.existsByMaNguoiDungAndTenMonHoc(userId, dto.getTenMonHoc())) {

            auditLogService.log(
                    userId,
                    "Thêm môn học thất bại (trùng tên): " + dto.getTenMonHoc(),
                    "that_bai"
            );

            throw new BadRequestException("Môn học đã tồn tại");
        }

        MonHocCaNhan entity = MonHocCaNhan.builder()
                .maNguoiDung(userId)
                .tenMonHoc(dto.getTenMonHoc())
                .moTa(dto.getMoTa())
                .mucDoHoc(dto.getMucDoHoc())
                .build();

        MonHocCaNhan saved = repository.save(entity);

        auditLogService.log(
                userId,
                "Thêm môn học cá nhân: " + saved.getTenMonHoc(),
                "thanh_cong"
        );
        return mapToResponse(saved);
    }

    @Override
    public List<MonHocResponseDTO> getMyMonHoc() {
        Integer userId = currentUserService.getCurrentUserId();

        return repository.findByMaNguoiDung(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MonHocResponseDTO mapToResponse(MonHocCaNhan entity) {
        return MonHocResponseDTO.builder()
                .maMonHoc(entity.getMaMonHoc())
                .tenMonHoc(entity.getTenMonHoc())
                .moTa(entity.getMoTa())
                .mucDoHoc(entity.getMucDoHoc())
                .ngayTao(entity.getNgayTao())
                .build();
    }
}
