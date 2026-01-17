package com.hoctap.learningsupportapi.service.summary;

import com.hoctap.learningsupportapi.model.entity.TomTatTuKhoa;
import com.hoctap.learningsupportapi.repository.TomTatTuKhoaRepository;
import com.hoctap.learningsupportapi.utils.summary.KeywordExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TomTatKeywordServiceImpl implements TomTatKeywordService {

    private final TomTatTuKhoaRepository repository;
    private final KeywordExtractor extractor;

    @Override
    public void generateAndSave(Integer maTomTat, String noiDungTomTat) {

        repository.deleteByMaTomTat(maTomTat); // tránh trùng khi regenerate

        List<String> keywords = extractor.extract(noiDungTomTat, 4);

        keywords.forEach(k ->
                repository.save(
                        TomTatTuKhoa.builder()
                                .maTomTat(maTomTat)
                                .tuKhoa(k)
                                .build()
                )
        );
    }

    @Override
    public List<String> getKeywords(Integer maTomTat) {
        return repository.findByMaTomTat(maTomTat)
                .stream()
                .map(TomTatTuKhoa::getTuKhoa)
                .toList();
    }
}
