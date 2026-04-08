package com.Aga.Agali.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotBlank(message = "Uşaq emaili boş ola bilməz")
    private String childEmail;

    @NotNull(message = "Məbləğ boş ola bilməz")
    @DecimalMin(value = "0.1", message = "Minimum 0.1 AZN ödənilə bilər")
    private BigDecimal amount;

    private String description;
}