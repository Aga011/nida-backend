package com.Aga.Agali.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParentResultDto {
    private Long id;
    private String studentEmail;
    private double percentage;
    private String analysis;
    private LocalDateTime createdAt;
    private LocalDateTime visibleAt;
}