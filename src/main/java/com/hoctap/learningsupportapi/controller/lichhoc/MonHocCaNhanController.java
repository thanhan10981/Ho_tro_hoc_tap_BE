package com.hoctap.learningsupportapi.controller.lichhoc;

import com.hoctap.learningsupportapi.model.dto.lichhoc.MonHocCreateDTO;
import com.hoctap.learningsupportapi.model.dto.lichhoc.MonHocResponseDTO;
import com.hoctap.learningsupportapi.service.lichhoc.MonHocCaNhanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mon-hoc")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MonHocCaNhanController {

    private final MonHocCaNhanService service;

    @PostMapping
    public MonHocResponseDTO create(@RequestBody MonHocCreateDTO dto) {
        return service.createMonHoc(dto);
    }

    @GetMapping("/me")
    public List<MonHocResponseDTO> getMyMonHoc() {
        return service.getMyMonHoc();
    }
}
