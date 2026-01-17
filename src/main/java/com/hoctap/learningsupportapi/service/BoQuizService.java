package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.CreateQuizRequest;
import com.hoctap.learningsupportapi.model.dto.QuizResponse;
import com.hoctap.learningsupportapi.model.entity.BoQuiz;
import com.hoctap.learningsupportapi.repository.BoQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoQuizService {

    private final BoQuizRepository boQuizRepository;

    public BoQuiz createQuiz(Integer userId, CreateQuizRequest request) {
        BoQuiz quiz = BoQuiz.builder()
                .maNguoiDung(userId)
                .maMonHoc(request.getMaMonHoc())
                .tenQuiz(request.getTenQuiz())
                .moTa(request.getMoTa())
                .ngayTao(LocalDateTime.now())
                .build();

        return boQuizRepository.save(quiz);
    }

    public List<QuizResponse> getMyQuizzes(Integer userId) {
        return boQuizRepository.findByMaNguoiDung(userId)
                .stream()
                .map(q -> QuizResponse.builder()
                        .maQuiz(q.getMaQuiz())
                        .tenQuiz(q.getTenQuiz())
                        .moTa(q.getMoTa())
                        .maMonHoc(q.getMaMonHoc())
                        .build())
                .toList();
    }
}
