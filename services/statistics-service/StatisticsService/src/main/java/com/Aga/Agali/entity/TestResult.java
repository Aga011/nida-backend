package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sessionId;
    private String userEmail;
    private String gradeLevel;
    private String specialtyGroup;
    private String sessionType;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private Long totalTimeSeconds;

    @CreationTimestamp
    private LocalDateTime createdAt;
}