package com.Aga.Agali.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private String parentEmail;
    private String childEmail;
    private String type;
    private String status;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
}