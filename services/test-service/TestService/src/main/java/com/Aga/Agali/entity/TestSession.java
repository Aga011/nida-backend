package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @Enumerated(EnumType.STRING)
    private SpecialtyGroup specialtyGroup;

    private int totalQuestions;
    private int correctAnswers;
    private boolean completed;

    private int lastQuestionIndex;

    private Long totalTimeSeconds;

    private boolean isAssessment;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}