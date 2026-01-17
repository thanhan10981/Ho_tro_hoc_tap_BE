package com.hoctap.learningsupportapi.service;
import com.hoctap.learningsupportapi.model.entity.BoFlashcard;
import com.hoctap.learningsupportapi.repository.BoFlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hoctap.learningsupportapi.model.dto.FlashcardSetResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoFlashcardService {

    private final BoFlashcardRepository boFlashcardRepository;

    public BoFlashcard createBoFlashcard(
            Integer userId,
            Integer maMonHoc,
            String tenBo,
            String moTa
    ) {
        BoFlashcard bo = new BoFlashcard();
        bo.setMaNguoiDung(userId);
        bo.setMaMonHoc(maMonHoc);
        bo.setTenBo(tenBo);
        bo.setMoTa(moTa);

        return boFlashcardRepository.save(bo);
    }


    public List<FlashcardSetResponse> getMyFlashcardSets(Integer userId) {
        return boFlashcardRepository
                .findFlashcardSetsWithCount(userId)
                .stream()
                .map(row -> {
                    FlashcardSetResponse dto = new FlashcardSetResponse();
                    dto.setMaBoFlashcard((Integer) row[0]);
                    dto.setTenBo((String) row[1]);
                    dto.setMoTa((String) row[2]);
                    dto.setSoLuongFlashcard(((Long) row[3]).intValue());
                    return dto;
                })
                .toList();
    }

}
