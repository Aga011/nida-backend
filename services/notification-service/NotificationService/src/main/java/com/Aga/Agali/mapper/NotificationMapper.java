package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.NotificationResponse;
import com.Aga.Agali.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .senderEmail(notification.getSenderEmail())
                .receiverEmail(notification.getReceiverEmail())
                .groupId(notification.getGroupId())
                .message(notification.getMessage())
                .type(notification.getType().name())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}