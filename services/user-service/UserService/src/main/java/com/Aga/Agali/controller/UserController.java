package com.Aga.Agali.controller;

import com.Aga.Agali.dto.UpdateProfileRequest;
import com.Aga.Agali.dto.UserDto;
import com.Aga.Agali.entity.User;
import com.Aga.Agali.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/unique/{uniqueId}")
    public ResponseEntity<UserDto> getByUniqueId(
            @PathVariable String uniqueId) {
        User user = userRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new RuntimeException("Şagird tapılmadı"));

        return ResponseEntity.ok(UserDto.builder()
                .uniqueId(user.getUniqueId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gradeLevel(user.getGradeLevel() != null
                        ? user.getGradeLevel().name() : null)
                .school(user.getSchool())
                .city(user.getCity())
                .build());
    }
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(
            @RequestHeader("X-User-Email") String email,
            @RequestBody @Valid UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        if (request.fullName() != null) user.setFullName(request.fullName());
        if (request.city() != null) user.setCity(request.city());
        if (request.school() != null) user.setSchool(request.school());
        if (request.subjectSpecialty() != null)
            user.setSubjectSpecialty(request.subjectSpecialty());

        userRepository.save(user);

        return ResponseEntity.ok(UserDto.builder()
                .uniqueId(user.getUniqueId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gradeLevel(user.getGradeLevel() != null
                        ? user.getGradeLevel().name() : null)
                .school(user.getSchool())
                .city(user.getCity())
                .build());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(
            @RequestHeader("X-User-Email") String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        return ResponseEntity.ok(UserDto.builder()
                .uniqueId(user.getUniqueId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .gradeLevel(user.getGradeLevel() != null
                        ? user.getGradeLevel().name() : null)
                .school(user.getSchool())
                .city(user.getCity())
                .build());
    }
}