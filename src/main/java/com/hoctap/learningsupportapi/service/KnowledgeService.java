package com.hoctap.learningsupportapi.service;


import com.hoctap.learningsupportapi.model.dto.KnowledgeDocResponse;
import com.hoctap.learningsupportapi.model.dto.KnowledgeSearchRequest;
import com.hoctap.learningsupportapi.model.dto.PersonalDocResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface KnowledgeService {

    Page<KnowledgeDocResponse> getCommonDocs(int page, int size);

    Page<KnowledgeDocResponse> searchFull(KnowledgeSearchRequest req);

    void saveToPersonal(Integer  userId, Integer   docId);

    List<PersonalDocResponse> getPersonalDocs(Integer userId);
}
