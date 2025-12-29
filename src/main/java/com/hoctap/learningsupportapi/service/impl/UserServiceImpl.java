package com.hoctap.learningsupportapi.service.impl;

import com.hoctap.learningsupportapi.exception.BadRequestException;
import com.hoctap.learningsupportapi.mapper.NguoiDungMapper;
import com.hoctap.learningsupportapi.model.dto.UserProfileResponse;
import com.hoctap.learningsupportapi.model.entity.NguoiDung;
import com.hoctap.learningsupportapi.repository.NguoiDungRepository;
import com.hoctap.learningsupportapi.service.CurrentUserService;
import com.hoctap.learningsupportapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final NguoiDungRepository nguoiDungRepository;
    private final CurrentUserService currentUserService;

    @Override
    public UserProfileResponse getCurrentUserProfile() {

        Integer userId = currentUserService.getCurrentUserId();

        NguoiDung user = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Người dùng không tồn tại"));

        return NguoiDungMapper.toProfileResponse(user);
    }
}
