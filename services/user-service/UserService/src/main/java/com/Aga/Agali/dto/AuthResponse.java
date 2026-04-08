package com.Aga.Agali.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String uniqueId,
        String fullName,
        String role,
        boolean emailVerified
) {}