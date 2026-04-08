package com.Aga.Agali.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        @NotBlank(message = "Token boş ola bilməz")
        String token,

        @NotBlank(message = "Şifrə boş ola bilməz")
        @Size(min = 8, message = "Şifrə minimum 8 simvol olmalıdır")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$",
                message = "Şifrə minimum 8 simvol, 1 böyük, 1 kiçik hərf, 1 rəqəm və 1 xüsusi simvoldan ibarət olmalıdır"
        )
        String newPassword
) {}