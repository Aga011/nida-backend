package com.Aga.Agali.repository;

import com.Aga.Agali.entity.LearningPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {
    List<LearningPlan> findByUserEmail(String userEmail);
    Optional<LearningPlan> findByUserEmailAndStage(String userEmail, String stage);
}