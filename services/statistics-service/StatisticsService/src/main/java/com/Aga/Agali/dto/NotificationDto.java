package com.Aga.Agali.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private String senderEmail;
    private String receiverEmail;
    private Long groupId;
    private String message;
    private String type;
}