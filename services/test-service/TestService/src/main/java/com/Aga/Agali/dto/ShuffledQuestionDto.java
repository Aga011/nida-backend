package com.Aga.Agali.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShuffledQuestionDto {
    private Long id;
    private String questionText;
    private String subject;
    private String gradeLevel;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String shuffleMap;
}