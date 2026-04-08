package com.Aga.Agali.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

    private String receiverEmail;
    private Long groupId;

    @NotBlank(message = "Mesaj boş ola bilməz")
    private String message;

    @NotBlank(message = "Tip boş ola bilməz")
    private String type;
}