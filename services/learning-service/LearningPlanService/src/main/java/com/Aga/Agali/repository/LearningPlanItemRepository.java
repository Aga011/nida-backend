package com.Aga.Agali.repository;

import com.Aga.Agali.entity.LearningPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPlanItemRepository extends JpaRepository<LearningPlanItem, Long> {
    List<LearningPlanItem> findByLearningPlanId(Long learningPlanId);
}