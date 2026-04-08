package com.Aga.Agali.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponse {
    private Long id;
    private String parentEmail;
    private BigDecimal amount;
    private LocalDateTime updatedAt;
}