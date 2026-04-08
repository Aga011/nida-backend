package com.Aga.Agali.repository;

import com.Aga.Agali.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByTargetEmail(String targetEmail);
    List<Permission> findByRequesterEmail(String requesterEmail);
    List<Permission> findByTargetEmailAndStatus(
            String targetEmail, PermissionStatus status);
    Optional<Permission> findByRequesterEmailAndTargetEmailAndSubjectAndType(
            String requesterEmail, String targetEmail,
            String subject, PermissionType type);
    List<Permission> findByRequesterEmailAndTypeAndStatus(
            String requesterEmail, PermissionType type, PermissionStatus status);
    int countByRequesterEmailAndTypeAndStatus(
            String requesterEmail, PermissionType type, PermissionStatus status);
}