package com.hoctap.learningsupportapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestionResponse {

    private Integer maCauHoi;
    private String noiDung;
    private List<AnswerResponse> dapAn;
    private Integer dapAnDung;

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerResponse {
        private Integer maDapAn;
        private String noiDung;
    }
}
