package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterEmail;

    private String targetEmail;

    private String subject;

    @Enumerated(EnumType.STRING)
    private PermissionType type;

    @Enumerated(EnumType.STRING)
    private PermissionStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
}