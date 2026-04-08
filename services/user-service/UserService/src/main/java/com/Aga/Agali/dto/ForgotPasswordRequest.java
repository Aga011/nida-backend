package com.Aga.Agali.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ForgotPasswordRequest(
        @Email
        @NotBlank(message = "Email boş ola bilməz")
        String email
) {}