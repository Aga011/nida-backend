package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "question_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private Long questionId;
    private Long sessionId;

    private String selectedAnswer;
    private boolean correct;
    private boolean skipped;


    private Long timeSpentSeconds;


    private String shuffleMap;

    private int attemptCount;
    private LocalDateTime nextReviewDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}