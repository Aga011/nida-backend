package com.Aga.Agali.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {

    @NotBlank(message = "Qrup adı boş ola bilməz")
    private String name;

    @NotBlank(message = "Fənn boş ola bilməz")
    private String subject;

    @Max(value = 30, message = "Maksimum 30 şagird ola bilər")
    private int maxSize;
}