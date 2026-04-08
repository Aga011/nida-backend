package com.Aga.Agali.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearningPlanRequest {
    private String gradeLevel;
    private String specialtyGroup;
    private String stage;
}