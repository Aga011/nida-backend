package com.Aga.Agali.service;

import com.Aga.Agali.client.UserServiceClient;
import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.mapper.PermissionMapper;
import com.Aga.Agali.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserServiceClient userServiceClient;
    private final PermissionMapper mapper;

    private static final int MAX_CHILDREN = 5;

    public PermissionResponse sendTeacherPermission(String teacherEmail,
                                                    PermissionRequest request) {
        UserDto student = userServiceClient
                .getUserByUniqueId(request.getTargetUniqueId());


        permissionRepository.findByRequesterEmailAndTargetEmailAndSubjectAndType(
                        teacherEmail, student.getEmail(),
                        request.getSubject(), PermissionType.TEACHER_SUBJECT)
                .ifPresent(p -> {
                    if (p.getStatus() == PermissionStatus.PENDING ||
                            p.getStatus() == PermissionStatus.APPROVED) {
                        throw new RuntimeException(
                                "Bu fənn üçün artıq icazə sorğusu mövcuddur");
                    }
                });

        Permission permission = Permission.builder()
                .requesterEmail(teacherEmail)
                .targetEmail(student.getEmail())
                .subject(request.getSubject())
                .type(PermissionType.TEACHER_SUBJECT)
                .status(PermissionStatus.PENDING)
                .build();

        return mapper.toResponse(permissionRepository.save(permission));
    }


    public PermissionResponse sendParentPermission(String parentEmail,
                                                   PermissionRequest request) {

        int childCount = permissionRepository.countByRequesterEmailAndTypeAndStatus(
                parentEmail, PermissionType.PARENT_CHILD, PermissionStatus.APPROVED);

        if (childCount >= MAX_CHILDREN) {
            throw new RuntimeException(
                    "Maksimum " + MAX_CHILDREN + " uşaq əlavə edə bilərsiniz");
        }

        UserDto student = userServiceClient
                .getUserByUniqueId(request.getTargetUniqueId());

        Permission permission = Permission.builder()
                .requesterEmail(parentEmail)
                .targetEmail(student.getEmail())
                .type(PermissionType.PARENT_CHILD)
                .status(PermissionStatus.PENDING)
                .build();

        return mapper.toResponse(permissionRepository.save(permission));
    }

    public PermissionResponse respondToPermission(Long permissionId,
                                                  String studentEmail,
                                                  boolean approved) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Sorğu tapılmadı"));

        if (!permission.getTargetEmail().equals(studentEmail)) {
            throw new RuntimeException("Bu sorğu sizə aid deyil");
        }

        if (permission.getStatus() != PermissionStatus.PENDING) {
            throw new RuntimeException("Bu sorğu artıq cavablandırılıb");
        }

        permission.setStatus(approved
                ? PermissionStatus.APPROVED
                : PermissionStatus.REJECTED);
        permission.setRespondedAt(LocalDateTime.now());

        return mapper.toResponse(permissionRepository.save(permission));
    }

    public PermissionResponse revokePermission(Long permissionId,
                                               String studentEmail) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Sorğu tapılmadı"));

        if (!permission.getTargetEmail().equals(studentEmail)) {
            throw new RuntimeException("Bu sorğu sizə aid deyil");
        }

        permission.setStatus(PermissionStatus.REVOKED);
        permission.setRespondedAt(LocalDateTime.now());

        return mapper.toResponse(permissionRepository.save(permission));
    }


    public List<PermissionResponse> getIncomingRequests(String studentEmail) {
        return permissionRepository
                .findByTargetEmailAndStatus(studentEmail, PermissionStatus.PENDING)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    public List<PermissionResponse> getSentRequests(String teacherEmail) {
        return permissionRepository.findByRequesterEmail(teacherEmail)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    public UserDto searchStudent(String uniqueId) {
        return userServiceClient.getUserByUniqueId(uniqueId);
    }
}