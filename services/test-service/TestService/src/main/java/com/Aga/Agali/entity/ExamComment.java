package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long examSessionId;
    private String teacherEmail;
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
}