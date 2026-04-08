package com.Aga.Agali.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private String studentEmail;
    private String teacherEmail;
    private String message;
    private String type;
}