package com.Aga.Agali.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {
    private String userEmail;
    private int totalTests;
    private double averagePercentage;
    private Long totalTimeSeconds;
    private Map<String, SubjectStatDto> subjectStats;
    private List<String> weakSubjects;
    private List<String> strongSubjects;
    private List<ProgressDto> weeklyProgress;
    private List<ProgressDto> monthlyProgress;
    private List<SuspiciousActivityDto> suspiciousActivities;
}