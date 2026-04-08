package com.Aga.Agali.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    private Long id;
    private String name;
    private String teacherEmail;
    private String subject;
    private int maxSize;
    private int currentSize;
    private List<String> members;
    private LocalDateTime createdAt;
}