package com.Aga.Agali.repository;

import com.Aga.Agali.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSession, Long> {
    List<TestSession> findByUserEmail(String userEmail);
    Optional<TestSession> findByUserEmailAndCompleted(String userEmail, boolean completed);
    Optional<TestSession> findByUserEmailAndSessionTypeAndCompleted(
            String userEmail, SessionType sessionType, boolean completed);
    Optional<TestSession> findByUserEmailAndGradeLevelAndSpecialtyGroupAndCompleted(
            String userEmail, GradeLevel gradeLevel,
            SpecialtyGroup specialtyGroup, boolean completed);
}