package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subject_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String subject;
    private int totalAnswered;
    private int correctAnswers;
    private double successRate;
    private String level;
    private Long totalTimeSeconds;
    private double avgTimePerQuestion;
}