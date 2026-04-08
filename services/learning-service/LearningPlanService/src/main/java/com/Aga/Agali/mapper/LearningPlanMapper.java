package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LearningPlanMapper {

    public TopicDto toTopicDto(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .name(topic.getName())
                .subject(topic.getSubject().name())
                .gradeLevel(topic.getGradeLevel().name())
                .orderIndex(topic.getOrderIndex())
                .build();
    }

    public LearningPlanItemDto toItemDto(LearningPlanItem item) {
        return LearningPlanItemDto.builder()
                .id(item.getId())
                .topic(toTopicDto(item.getTopic()))
                .priority(item.getPriority())
                .completed(item.isCompleted())
                .priorityReason(item.getPriorityReason())
                .build();
    }

    public LearningPlanResponse toPlanResponse(LearningPlan plan,
                                               List<LearningPlanItem> items) {
        long completedCount = items.stream()
                .filter(LearningPlanItem::isCompleted)
                .count();
        double completionPercentage = items.isEmpty() ? 0 :
                (double) completedCount / items.size() * 100;

        return LearningPlanResponse.builder()
                .planId(plan.getId())
                .userEmail(plan.getUserEmail())
                .gradeLevel(plan.getGradeLevel().name())
                .specialtyGroup(plan.getSpecialtyGroup().name())
                .stage(plan.getStage())
                .completionPercentage(completionPercentage)
                .items(items.stream()
                        .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
                        .map(this::toItemDto)
                        .toList())
                .build();
    }
}