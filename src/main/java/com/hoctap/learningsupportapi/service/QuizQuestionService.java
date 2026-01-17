package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.QuizQuestionResponse;
import com.hoctap.learningsupportapi.model.entity.CauHoiQuiz;
import com.hoctap.learningsupportapi.model.entity.DapAnQuiz;
import com.hoctap.learningsupportapi.repository.CauHoiQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizQuestionService {

    private final CauHoiQuizRepository cauHoiRepo;

    public List<QuizQuestionResponse> getQuestionsByQuiz(Integer maQuiz) {

        List<CauHoiQuiz> questions = cauHoiRepo.findByMaQuiz(maQuiz);

        return questions.stream().map(q -> {

            List<QuizQuestionResponse.AnswerResponse> answers =
                    q.getDapAn().stream()
                            .map(a -> new QuizQuestionResponse.AnswerResponse(
                                    a.getMaDapAn(),
                                    a.getNoiDung()
                            ))
                            .toList();

            Integer dapAnDung = q.getDapAn().stream()
                    .filter(DapAnQuiz::getIsDung)
                    .findFirst()
                    .map(DapAnQuiz::getMaDapAn)
                    .orElse(null);

            return new QuizQuestionResponse(
                    q.getMaCauHoi(),
                    q.getNoiDung(),
                    answers,
                    dapAnDung
            );
        }).toList();
    }
}
