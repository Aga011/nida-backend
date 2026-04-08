package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.PermissionResponse;
import com.Aga.Agali.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionResponse toResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .requesterEmail(permission.getRequesterEmail())
                .targetEmail(permission.getTargetEmail())
                .subject(permission.getSubject())
                .type(permission.getType().name())
                .status(permission.getStatus().name())
                .createdAt(permission.getCreatedAt())
                .respondedAt(permission.getRespondedAt())
                .build();
    }
}