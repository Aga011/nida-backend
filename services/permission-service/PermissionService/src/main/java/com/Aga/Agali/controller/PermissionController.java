package com.Aga.Agali.controller;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final GroupService groupService;

    @GetMapping("/search/{uniqueId}")
    public ResponseEntity<UserDto> searchStudent(
            @PathVariable String uniqueId) {
        return ResponseEntity.ok(permissionService.searchStudent(uniqueId));
    }

    @PostMapping("/teacher/send")
    public ResponseEntity<PermissionResponse> sendTeacherPermission(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid PermissionRequest request) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər icazə sorğusu göndərə bilər");
        }
        return ResponseEntity.ok(
                permissionService.sendTeacherPermission(teacherEmail, request));
    }

    @PostMapping("/parent/send")
    public ResponseEntity<PermissionResponse> sendParentPermission(
            @RequestHeader("X-User-Email") String parentEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid PermissionRequest request) {
        if (!role.equals("PARENT")) {
            throw new RuntimeException("Yalnız valideynlər izlə sorğusu göndərə bilər");
        }
        return ResponseEntity.ok(
                permissionService.sendParentPermission(parentEmail, request));
    }

    @PutMapping("/respond/{permissionId}")
    public ResponseEntity<PermissionResponse> respond(
            @RequestHeader("X-User-Email") String studentEmail,
            @PathVariable Long permissionId,
            @RequestParam boolean approved) {
        return ResponseEntity.ok(
                permissionService.respondToPermission(
                        permissionId, studentEmail, approved));
    }

    @PutMapping("/revoke/{permissionId}")
    public ResponseEntity<PermissionResponse> revoke(
            @RequestHeader("X-User-Email") String studentEmail,
            @PathVariable Long permissionId) {
        return ResponseEntity.ok(
                permissionService.revokePermission(permissionId, studentEmail));
    }

    @GetMapping("/incoming")
    public ResponseEntity<List<PermissionResponse>> getIncoming(
            @RequestHeader("X-User-Email") String studentEmail) {
        return ResponseEntity.ok(
                permissionService.getIncomingRequests(studentEmail));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<PermissionResponse>> getSent(
            @RequestHeader("X-User-Email") String teacherEmail) {
        return ResponseEntity.ok(
                permissionService.getSentRequests(teacherEmail));
    }

    @PostMapping("/group/create")
    public ResponseEntity<GroupResponse> createGroup(
            @RequestHeader("X-User-Email") String teacherEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid GroupRequest request) {
        if (!role.equals("TEACHER")) {
            throw new RuntimeException("Yalnız müəllimlər qrup yarada bilər");
        }
        return ResponseEntity.ok(
                groupService.createGroup(teacherEmail, request));
    }

    @PostMapping("/group/{groupId}/invite")
    public ResponseEntity<GroupResponse> inviteStudent(
            @RequestHeader("X-User-Email") String teacherEmail,
            @PathVariable Long groupId,
            @RequestParam String studentEmail) {
        return ResponseEntity.ok(
                groupService.inviteStudent(groupId, teacherEmail, studentEmail));
    }

    @PutMapping("/group/{groupId}/respond")
    public ResponseEntity<GroupResponse> respondToInvite(
            @RequestHeader("X-User-Email") String studentEmail,
            @PathVariable Long groupId,
            @RequestParam boolean accepted) {
        return ResponseEntity.ok(
                groupService.respondToInvite(groupId, studentEmail, accepted));
    }

    @GetMapping("/group/my")
    public ResponseEntity<List<GroupResponse>> getMyGroups(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-User-Role") String role) {
        if (role.equals("TEACHER")) {
            return ResponseEntity.ok(groupService.getTeacherGroups(userEmail));
        }
        return ResponseEntity.ok(groupService.getStudentGroups(userEmail));
    }
}