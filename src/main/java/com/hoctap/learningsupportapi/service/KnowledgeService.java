package com.hoctap.learningsupportapi.service;


import com.hoctap.learningsupportapi.model.dto.*;
import com.hoctap.learningsupportapi.model.entity.TaiLieuNhan;
import org.springframework.data.domain.Page;

import java.util.List;

public interface KnowledgeService {

    Page<KnowledgeDocResponse> getCommonDocs(int page, int size);

    Page<KnowledgeDocResponse> searchFull(KnowledgeSearchRequest req);

    void saveToPersonal(Integer  userId, Integer   docId);
    void removeFromPersonal(Integer userId, Integer docId);
    List<PersonalDocResponse> getPersonalDocs(Integer userId);
    void addNhanToPersonalDoc(Integer userId, Integer docId, Integer nhanId);
    List<PersonalDocResponse> getDocsByNhan(Integer nhanId);
    boolean isSaved(Integer userId, Integer docId);
    Double getAvgRating(Integer docId);
    Integer getTotalRating(Integer docId);
    KnowledgeDocDetailResponse getDocDetail(Integer docId);
    List<SidebarStatResponse> sidebarByLinhVuc();
}
