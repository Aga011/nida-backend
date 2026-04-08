package com.Aga.Agali.service;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.mapper.NotificationMapper;
import com.Aga.Agali.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;
    private final EmailService emailService;

    public void processNotification(NotificationDto dto) {
        Notification notification = Notification.builder()
                .senderEmail(dto.getSenderEmail())
                .receiverEmail(dto.getReceiverEmail())
                .groupId(dto.getGroupId())
                .message(dto.getMessage())
                .type(NotificationType.valueOf(dto.getType()))
                .read(false)
                .build();

        notificationRepository.save(notification);

        if (dto.getReceiverEmail() != null) {
            String subject = getEmailSubject(dto.getType());
            emailService.sendNotificationEmail(
                    dto.getReceiverEmail(),
                    subject,
                    dto.getMessage());
        }
    }

    public NotificationResponse sendMessage(String senderEmail,
                                            SendMessageRequest request) {
        if (request.getReceiverEmail() == null && request.getGroupId() == null) {
            throw new RuntimeException(
                    "Alıcı email və ya qrup ID-si daxil edilməlidir");
        }

        Notification notification = Notification.builder()
                .senderEmail(senderEmail)
                .receiverEmail(request.getReceiverEmail())
                .groupId(request.getGroupId())
                .message(request.getMessage())
                .type(NotificationType.valueOf(request.getType()))
                .read(false)
                .build();

        notificationRepository.save(notification);

        if (request.getReceiverEmail() != null) {
            emailService.sendNotificationEmail(
                    request.getReceiverEmail(),
                    "Yeni mesaj",
                    request.getMessage());
        }

        return mapper.toResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponse> getMyNotifications(String userEmail) {
        return notificationRepository.findByReceiverEmail(userEmail)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<NotificationResponse> getUnreadNotifications(String userEmail) {
        return notificationRepository
                .findByReceiverEmailAndRead(userEmail, false)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<NotificationResponse> getSentMessages(String teacherEmail) {
        return notificationRepository.findBySenderEmail(teacherEmail)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    public List<NotificationResponse> getGroupMessages(Long groupId) {
        return notificationRepository.findByGroupId(groupId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bildiriş tapılmadı"));

        notification.setRead(true);
        return mapper.toResponse(notificationRepository.save(notification));
    }

    public void markAllAsRead(String userEmail) {
        List<Notification> unread = notificationRepository
                .findByReceiverEmailAndRead(userEmail, false);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    private String getEmailSubject(String type) {
        return switch (type) {
            case "TEST_COMPLETED" -> "Test nəticəniz hazırdır";
            case "WEAK_SUBJECT_ALERT" -> "Zəif fənn bildirişi";
            case "LEARNING_PLAN_UPDATED" -> "Öyrənmə planınız yeniləndi";
            case "TEACHER_MESSAGE" -> "Müəlliminizdən yeni mesaj";
            case "GROUP_MESSAGE" -> "Qrupunuzdan yeni mesaj";
            default -> "Yeni bildiriş";
        };
    }
}