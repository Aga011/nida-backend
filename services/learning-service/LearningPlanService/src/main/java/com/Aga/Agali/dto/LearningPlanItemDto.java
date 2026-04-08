package com.Aga.Agali.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LearningPlanItemDto {
    private Long id;
    private TopicDto topic;
    private int priority;
    private boolean completed;
    private String priorityReason;
}