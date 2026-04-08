package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.GroupResponse;
import com.Aga.Agali.entity.Group;
import com.Aga.Agali.entity.GroupMember;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {

    public GroupResponse toResponse(Group group,
                                    List<GroupMember> members,
                                    int currentSize) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .teacherEmail(group.getTeacherEmail())
                .subject(group.getSubject())
                .maxSize(group.getMaxSize())
                .currentSize(currentSize)
                .members(members.stream()
                        .map(GroupMember::getStudentEmail)
                        .toList())
                .createdAt(group.getCreatedAt())
                .build();
    }
}