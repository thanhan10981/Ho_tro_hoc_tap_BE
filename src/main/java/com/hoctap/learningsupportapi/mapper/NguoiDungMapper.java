package com.hoctap.learningsupportapi.mapper;

import com.hoctap.learningsupportapi.model.dto.UserProfileResponse;
import com.hoctap.learningsupportapi.model.dto.UserResponse;
import com.hoctap.learningsupportapi.model.entity.NguoiDung;

public class NguoiDungMapper {

    public static UserResponse toResponse(NguoiDung user) {
        return UserResponse.builder()
                .id(user.getId())
                .hoTen(user.getHoTen())
                .email(user.getEmail())
                .linhVucQuanTam(user.getLinhVucQuanTam())
                .ngayTao(user.getNgayTao())
                .build();
    }

    public static UserProfileResponse toProfileResponse(NguoiDung user) {
        return UserProfileResponse.builder()
                .hoTen(user.getHoTen())
                .email(user.getEmail())
                .capBacHoc(user.getCapBacHoc())
                .linhVucQuanTam(user.getLinhVucQuanTam())
                .ngayTao(user.getNgayTao())
                .build();
    }

}
