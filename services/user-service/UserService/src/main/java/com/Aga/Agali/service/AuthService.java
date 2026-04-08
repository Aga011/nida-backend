package com.Aga.Agali.service;

import com.Aga.Agali.client.TestServiceClient;
import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.ROLE;
import com.Aga.Agali.entity.User;
import com.Aga.Agali.mapper.UserMapper;
import com.Aga.Agali.repository.UserRepository;
import com.Aga.Agali.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final EmailService emailService;
    private final TestServiceClient testServiceClient;


    public AuthResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new RuntimeException("Bu email artıq qeydiyyatdan keçib");
        }

        validateRoleFields(request);

        String verificationToken = UUID.randomUUID().toString();
        User user = mapper.toEntity(request, encoder.encode(request.password()));
        user.setVerificationToken(verificationToken);

        repository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        if (request.role() == ROLE.STUDENT) {
            testServiceClient.startAssessment(
                    user.getEmail(),
                    user.getGradeLevel().name());
        }

        String token = jwtService.generateToken(user);
        return mapper.toResponse(user, token);
    }

    public String verifyEmail(String token) {
        User user = repository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token yanlışdır və ya müddəti bitib"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        repository.save(user);

        return "Email uğurla təsdiqləndi";
    }

    public AuthResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email və ya şifrə yanlışdır"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Zəhmət olmasa əvvəlcə emailinizi təsdiqləyin");
        }

        manager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));

        String token = jwtService.generateToken(user);
        return mapper.toResponse(user, token);
    }
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Bu email tapılmadı"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(30));
        repository.save(user);

        emailService.sendResetPasswordEmail(user.getEmail(), resetToken);
        return "Şifrə sıfırlama linki emailinizə göndərildi";
    }

    public String resetPassword(ResetPasswordRequest request) {
        User user = repository.findByResetPasswordToken(request.token())
                .orElseThrow(() -> new RuntimeException("Token yanlışdır"));

        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Tokenin müddəti bitib");
        }

        user.setPassword(encoder.encode(request.newPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        repository.save(user);

        return "Şifrəniz uğurla dəyişdirildi";
    }

    private void validateRoleFields(RegisterRequest request) {
        if (request.role() == ROLE.STUDENT) {
            if (request.gradeLevel() == null)
                throw new RuntimeException("Şagird üçün sinif seçilməlidir");
            if (request.foreignLanguage() == null)
                throw new RuntimeException("Şagird üçün xarici dil seçilməlidir");
            if (request.city() == null || request.city().isBlank())
                throw new RuntimeException("Şəhər daxil edilməlidir");
            if (request.school() == null || request.school().isBlank())
                throw new RuntimeException("Məktəb daxil edilməlidir");
        }

        if (request.role() == ROLE.TEACHER) {
            if (request.subjectSpecialty() == null || request.subjectSpecialty().isBlank())
                throw new RuntimeException("Müəllim üçün ixtisas fənni daxil edilməlidir");
        }

    }
}