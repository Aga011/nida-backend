package com.Aga.Agali.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressDto {
    private LocalDateTime date;
    private double percentage;
    private int totalQuestions;
    private int correctAnswers;
    private Long timeSpentSeconds;
    private String sessionType;
}