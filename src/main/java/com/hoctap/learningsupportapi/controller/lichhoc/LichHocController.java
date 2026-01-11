package com.hoctap.learningsupportapi.controller.lichhoc;


import com.hoctap.learningsupportapi.model.dto.lichhoc.CreateEventRequest;
import com.hoctap.learningsupportapi.model.dto.lichhoc.LichHocCalendarDTO;
import com.hoctap.learningsupportapi.service.lichhoc.LichHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lich-hoc")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LichHocController {

    private final LichHocService lichHocService;


    @GetMapping("/calendar")
    public List<LichHocCalendarDTO> getCalendarEvents(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        return lichHocService.getLichHocTrongKhoang(fromDate, toDate);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateEventRequest request) {
        lichHocService.createEvent(request);
        return ResponseEntity.ok().build();
    }
}
