package com.Aga.Agali.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamRequest {

    @NotBlank(message = "Başlıq boş ola bilməz")
    private String title;

    private String description;

    @NotBlank(message = "Fənn boş ola bilməz")
    private String subject;

    @NotBlank(message = "Sinif boş ola bilməz")
    private String gradeLevel;

    @Min(value = 10, message = "Minimum 10 dəqiqə olmalıdır")
    @Max(value = 180, message = "Maksimum 180 dəqiqə olmalıdır")
    private int durationMinutes;

    private Long groupId;
}