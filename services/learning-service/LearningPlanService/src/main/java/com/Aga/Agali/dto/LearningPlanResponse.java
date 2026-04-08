package com.Aga.Agali.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearningPlanResponse {
    private Long planId;
    private String userEmail;
    private String gradeLevel;
    private String specialtyGroup;
    private String stage;
    private double completionPercentage;
    private List<LearningPlanItemDto> items;
}