package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.AuthResponse;
import com.Aga.Agali.dto.RegisterRequest;
import com.Aga.Agali.entity.ROLE;
import com.Aga.Agali.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request, String encodedPassword) {
        User.UserBuilder builder = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(encodedPassword)
                .role(request.role())
                .emailVerified(false);

        if (request.role() == ROLE.STUDENT) {
            builder
                    .gradeLevel(request.gradeLevel())
                    .foreignLanguage(request.foreignLanguage())
                    .city(request.city())
                    .school(request.school());
        }

        if (request.role() == ROLE.TEACHER) {
            builder
                    .subjectSpecialty(request.subjectSpecialty())
                    .city(request.city())
                    .school(request.school());
        }

        return builder.build();
    }

    public AuthResponse toResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .uniqueId(user.getUniqueId())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}