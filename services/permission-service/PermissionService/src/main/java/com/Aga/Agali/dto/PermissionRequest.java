package com.Aga.Agali.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {

    @NotBlank(message = "Şagird ID-si boş ola bilməz")
    private String targetUniqueId;

    private String subject;

    @NotBlank(message = "Tip boş ola bilməz")
    private String type;
}