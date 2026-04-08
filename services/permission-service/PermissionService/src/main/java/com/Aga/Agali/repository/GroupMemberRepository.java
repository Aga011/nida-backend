package com.Aga.Agali.repository;

import com.Aga.Agali.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByGroupId(Long groupId);
    List<GroupMember> findByStudentEmail(String studentEmail);
    List<GroupMember> findByGroupIdAndStatus(Long groupId, PermissionStatus status);
    int countByGroupIdAndStatus(Long groupId, PermissionStatus status);
    Optional<GroupMember> findByGroupIdAndStudentEmail(
            Long groupId, String studentEmail);
}