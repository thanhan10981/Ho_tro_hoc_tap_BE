package com.hoctap.learningsupportapi.controller;

import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import com.hoctap.learningsupportapi.repository.NguoiDungRepository;
import com.hoctap.learningsupportapi.service.PersonalLibraryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-library")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PersonalLibraryController {

    private final PersonalLibraryQueryService service;
    private final NguoiDungRepository nguoiDungRepo;

    @GetMapping
    public List<PersonalDocResponse> getMyLibrary(
            Authentication authentication
    ) {
        String email = authentication.getName();

        Integer userId = nguoiDungRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getId();

        return service.getPersonalLibrary(userId);
    }
    @GetMapping("/{docId}")
    public PersonalDocResponse getPersonalDocDetail(
            @PathVariable Integer docId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        Integer userId = nguoiDungRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getId();

        return service.getPersonalDocDetail(userId, docId);
    }

}
