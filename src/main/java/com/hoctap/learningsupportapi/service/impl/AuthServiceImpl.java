package com.hoctap.learningsupportapi.service.impl;

import com.hoctap.learningsupportapi.mapper.NguoiDungMapper;
import com.hoctap.learningsupportapi.model.dto.LoginRequest;
import com.hoctap.learningsupportapi.model.dto.LoginResponse;
import com.hoctap.learningsupportapi.model.dto.RegisterRequest;
import com.hoctap.learningsupportapi.model.dto.UserResponse;
import com.hoctap.learningsupportapi.model.entity.AuditLog;
import com.hoctap.learningsupportapi.model.entity.NguoiDung;
import com.hoctap.learningsupportapi.repository.AuditLogRepository;
import com.hoctap.learningsupportapi.repository.NguoiDungRepository;
import com.hoctap.learningsupportapi.service.AuthService;
import com.hoctap.learningsupportapi.utils.JwtUtil;
import com.hoctap.learningsupportapi.utils.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.hoctap.learningsupportapi.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final NguoiDungRepository nguoiDungRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        if (!request.getMatKhau().equals(request.getXacNhanMatKhau())) {
            throw new BadRequestException("Mật khẩu xác nhận không khớp");
        }

        if (nguoiDungRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã tồn tại");
        }

        NguoiDung user = NguoiDung.builder()
                .hoTen(request.getHoTen())
                .email(request.getEmail())
                .matKhau(PasswordUtil.hashPassword(request.getMatKhau()))
                .linhVucQuanTam(request.getLinhVucQuanTam())
                .build();

        nguoiDungRepository.save(user);

        return NguoiDungMapper.toResponse(user);
    }


    @Override
    public LoginResponse login(LoginRequest request) {

        NguoiDung user = nguoiDungRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Email không tồn tại"));

        boolean match = PasswordUtil.matches(
                request.getMatKhau(),
                user.getMatKhau()
        );

        if (!match) {
            saveLog(user.getId(), "Đăng nhập thất bại", "that_bai");
            throw new BadRequestException("Mật khẩu không đúng");
        }

        String token = JwtUtil.generateToken(user.getId(), user.getEmail());

        saveLog(user.getId(), "Đăng nhập thành công", "thanh_cong");

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    private void saveLog(Integer userId, String moTa, String trangThai) {
        auditLogRepository.save(
                AuditLog.builder()
                        .maNguoiDung(userId)
                        .moTa(moTa)
                        .trangThai(trangThai)
                        .build()
        );
    }


}
