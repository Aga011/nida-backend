package com.Aga.Agali.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamSessionResponse {
    private Long id;
    private Long examId;
    private String studentEmail;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private boolean completed;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String teacherComment;
    private String comment;
}