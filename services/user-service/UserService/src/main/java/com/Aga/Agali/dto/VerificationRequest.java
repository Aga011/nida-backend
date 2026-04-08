package com.Aga.Agali.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record VerificationRequest(
        @NotBlank(message = "Token boş ola bilməz")
        String token
) {}