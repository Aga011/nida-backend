package com.Aga.Agali.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuspiciousActivityDto {
    private String userEmail;
    private Long questionId;
    private Long timeSpentSeconds;
    private LocalDateTime detectedAt;
    private String reason;
}