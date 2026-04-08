package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long examId;
    private String studentEmail;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private boolean completed;

    @CreationTimestamp
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}