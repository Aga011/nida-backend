package com.Aga.Agali.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String uniqueId;
    private String fullName;
    private String email;
    private String gradeLevel;
    private String school;
    private String city;
}