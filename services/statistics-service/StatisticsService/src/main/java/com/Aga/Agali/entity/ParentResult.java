package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "parent_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentEmail;
    private Long sessionId;
    private double percentage;
    private String analysis;
    private boolean visible;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime visibleAt;
}