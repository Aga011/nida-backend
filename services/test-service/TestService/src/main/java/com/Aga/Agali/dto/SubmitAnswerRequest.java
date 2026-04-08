package com.Aga.Agali.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerRequest {
    private Long questionId;
    private String selectedAnswer;
    private boolean skipped;
    private Long timeSpentSeconds;
}