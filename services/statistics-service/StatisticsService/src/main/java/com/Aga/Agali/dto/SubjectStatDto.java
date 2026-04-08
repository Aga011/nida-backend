package com.Aga.Agali.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectStatDto {
    private String subject;
    private int totalAnswered;
    private int correctAnswers;
    private double successRate;
    private String level;
    private Long totalTimeSeconds;
    private double avgTimePerQuestion;
}