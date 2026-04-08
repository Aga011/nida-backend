package com.Aga.Agali.dto;

import com.Aga.Agali.entity.ForeignLanguage;
import com.Aga.Agali.entity.GradeLevel;
import com.Aga.Agali.entity.ROLE;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record RegisterRequest(

        @NotBlank(message = "Ad boş ola bilməz")
        @Size(min = 5, max = 40, message = "5-40 simvol arası olmalıdır")
        String fullName,

        @NotBlank(message = "Email boş ola bilməz")
        @Email(message = "Email formatı yanlışdır")
        String email,

        @NotBlank(message = "Şifrə boş ola bilməz")
        @Size(min = 8, message = "Şifrə minimum 8 simvol olmalıdır")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$",
                message = "Şifrə minimum 8 simvol, 1 böyük, 1 kiçik hərf, 1 rəqəm və 1 xüsusi simvoldan ibarət olmalıdır"
        )
        String password,

        @NotNull(message = "Rol seçilməlidir")
        ROLE role,

        GradeLevel gradeLevel,
        ForeignLanguage foreignLanguage,
        String city,
        String school,


        String subjectSpecialty
) {}