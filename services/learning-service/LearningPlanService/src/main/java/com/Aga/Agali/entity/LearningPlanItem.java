package com.Aga.Agali.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "learning_plan_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "learning_plan_id")
    private LearningPlan learningPlan;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private int priority;
    private boolean completed;

    private String priorityReason;
}