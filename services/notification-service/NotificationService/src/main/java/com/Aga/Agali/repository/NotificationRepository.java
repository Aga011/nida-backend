package com.Aga.Agali.repository;

import com.Aga.Agali.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverEmail(String receiverEmail);
    List<Notification> findByReceiverEmailAndRead(String receiverEmail, boolean read);
    List<Notification> findBySenderEmail(String senderEmail);
    List<Notification> findByGroupId(Long groupId);
}