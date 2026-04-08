package com.Aga.Agali.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String senderEmail;
    private String receiverEmail;
    private Long groupId;
    private String message;
    private String type;
    private boolean read;
    private LocalDateTime createdAt;
}