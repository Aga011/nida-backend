package com.Aga.Agali.controller;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendMessage(
            @RequestHeader("X-User-Email") String senderEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid SendMessageRequest request) {
        if (!role.equals("TEACHER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Yalnız müəllimlər mesaj göndərə bilər");
        }
        return ResponseEntity.ok(
                notificationService.sendMessage(senderEmail, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(
                notificationService.getMyNotifications(userEmail));
    }


    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userEmail));
    }


    @GetMapping("/sent")
    public ResponseEntity<List<NotificationResponse>> getSentMessages(
            @RequestHeader("X-User-Email") String teacherEmail) {
        return ResponseEntity.ok(
                notificationService.getSentMessages(teacherEmail));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<NotificationResponse>> getGroupMessages(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(
                notificationService.getGroupMessages(groupId));
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PutMapping("/read/all")
    public ResponseEntity<Void> markAllAsRead(
            @RequestHeader("X-User-Email") String userEmail) {
        notificationService.markAllAsRead(userEmail);
        return ResponseEntity.ok().build();
    }
}