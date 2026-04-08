package com.Aga.Agali.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamCommentRequest {

    @NotBlank(message = "Şərh boş ola bilməz")
    private String comment;
}