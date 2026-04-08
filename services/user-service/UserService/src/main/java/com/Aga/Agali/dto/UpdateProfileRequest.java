package com.Aga.Agali.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateProfileRequest(
        @Size(min = 5, max = 40, message = "5-40 simvol arası olmalıdır")
        String fullName,
        String city,
        String school,
        String subjectSpecialty
) {}