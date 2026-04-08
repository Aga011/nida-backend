package com.Aga.Agali.service;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.mapper.GroupMapper;
import com.Aga.Agali.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final PermissionRepository permissionRepository;
    private final GroupMapper groupMapper;

    public GroupResponse createGroup(String teacherEmail, GroupRequest request) {
        Group group = Group.builder()
                .name(request.getName())
                .teacherEmail(teacherEmail)
                .subject(request.getSubject())
                .maxSize(request.getMaxSize() > 0 ? request.getMaxSize() : 30)
                .build();

        return groupMapper.toResponse(groupRepository.save(group),
                List.of(), 0);
    }

    public GroupResponse inviteStudent(Long groupId,
                                       String teacherEmail,
                                       String studentEmail) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Qrup tapılmadı"));

        if (!group.getTeacherEmail().equals(teacherEmail)) {
            throw new RuntimeException("Bu qrup sizə aid deyil");
        }

        int currentSize = memberRepository.countByGroupIdAndStatus(
                groupId, PermissionStatus.APPROVED);
        if (currentSize >= group.getMaxSize()) {
            throw new RuntimeException("Qrup doludur — maksimum "
                    + group.getMaxSize() + " nəfər");
        }

        memberRepository.findByGroupIdAndStudentEmail(groupId, studentEmail)
                .ifPresent(m -> {
                    throw new RuntimeException("Şagird artıq bu qrupda mövcuddur");
                });

        GroupMember member = GroupMember.builder()
                .groupId(groupId)
                .studentEmail(studentEmail)
                .status(PermissionStatus.PENDING)
                .build();

        memberRepository.save(member);

        List<GroupMember> members = memberRepository.findByGroupId(groupId);
        return groupMapper.toResponse(group, members, currentSize);
    }

    public GroupResponse respondToInvite(Long groupId,
                                         String studentEmail,
                                         boolean accepted) {
        GroupMember member = memberRepository
                .findByGroupIdAndStudentEmail(groupId, studentEmail)
                .orElseThrow(() -> new RuntimeException("Dəvət tapılmadı"));

        member.setStatus(accepted
                ? PermissionStatus.APPROVED
                : PermissionStatus.REJECTED);
        memberRepository.save(member);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Qrup tapılmadı"));

        int currentSize = memberRepository.countByGroupIdAndStatus(
                groupId, PermissionStatus.APPROVED);
        List<GroupMember> members = memberRepository.findByGroupId(groupId);

        return groupMapper.toResponse(group, members, currentSize);
    }

    public List<GroupResponse> getTeacherGroups(String teacherEmail) {
        return groupRepository.findByTeacherEmail(teacherEmail)
                .stream()
                .map(group -> {
                    List<GroupMember> members =
                            memberRepository.findByGroupId(group.getId());
                    int size = memberRepository.countByGroupIdAndStatus(
                            group.getId(), PermissionStatus.APPROVED);
                    return groupMapper.toResponse(group, members, size);
                })
                .toList();
    }

    public List<GroupResponse> getStudentGroups(String studentEmail) {
        return memberRepository.findByStudentEmail(studentEmail)
                .stream()
                .filter(m -> m.getStatus() == PermissionStatus.APPROVED)
                .map(m -> {
                    Group group = groupRepository.findById(m.getGroupId())
                            .orElseThrow();
                    List<GroupMember> members =
                            memberRepository.findByGroupId(group.getId());
                    int size = memberRepository.countByGroupIdAndStatus(
                            group.getId(), PermissionStatus.APPROVED);
                    return groupMapper.toResponse(group, members, size);
                })
                .toList();
    }
}