package com.Aga.Agali.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponse {
    private Long id;
    private String requesterEmail;
    private String targetEmail;
    private String subject;
    private String type;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
}