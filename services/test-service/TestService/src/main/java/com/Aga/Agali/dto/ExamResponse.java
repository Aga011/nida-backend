package com.Aga.Agali.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponse {
    private Long id;
    private String teacherEmail;
    private String title;
    private String description;
    private String subject;
    private String gradeLevel;
    private String status;
    private int durationMinutes;
    private boolean paid;
    private Long groupId;
    private int questionCount;
    private LocalDateTime createdAt;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}