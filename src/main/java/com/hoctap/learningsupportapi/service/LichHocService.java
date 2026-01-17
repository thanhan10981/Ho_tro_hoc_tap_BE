package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.UpcomingEventDto;
import com.hoctap.learningsupportapi.repository.LichHocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LichHocService {

    private final LichHocRepository lichHocRepository;

    public List<UpcomingEventDto> getUpcomingEvents(Integer userId) {
        return lichHocRepository.findUpcomingEvents(userId);
    }

}
