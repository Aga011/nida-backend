package com.Aga.Agali.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopUpRequest {

    @NotNull(message = "Məbləğ boş ola bilməz")
    @DecimalMin(value = "1.0", message = "Minimum 1 AZN artırıla bilər")
    @DecimalMax(value = "1000.0", message = "Maksimum 1000 AZN artırıla bilər")
    private BigDecimal amount;
}