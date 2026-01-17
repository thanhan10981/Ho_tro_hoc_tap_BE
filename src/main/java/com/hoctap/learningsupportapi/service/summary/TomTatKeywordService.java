package com.hoctap.learningsupportapi.service.summary;

import java.util.List;

public interface TomTatKeywordService {

    void generateAndSave(Integer maTomTat, String noiDungTomTat);

    List<String> getKeywords(Integer maTomTat);
}
