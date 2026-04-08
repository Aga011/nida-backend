package com.Aga.Agali.dto;

import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestResultDto {
    private Long sessionId;
    private String userEmail;
    private String gradeLevel;
    private String specialtyGroup;
    private String sessionType;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private Long totalTimeSeconds;
    private Map<String, Integer> subjectResults;
    private Map<String, Long> subjectTimeResults;
}