package com.Aga.Agali.dto;

import com.Aga.Agali.entity.GradeLevel;
import com.Aga.Agali.entity.SpecialtyGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestResultDto {
    private Long sessionId;
    private String userEmail;
    private GradeLevel gradeLevel;
    private SpecialtyGroup specialtyGroup;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private Map<String, Integer> subjectResults;
}