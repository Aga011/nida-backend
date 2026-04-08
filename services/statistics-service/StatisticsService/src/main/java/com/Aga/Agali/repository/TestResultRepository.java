package com.Aga.Agali.repository;

import com.Aga.Agali.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUserEmail(String userEmail);
    List<TestResult> findByUserEmailAndCreatedAtBetween(
            String userEmail, LocalDateTime from, LocalDateTime to);
    boolean existsBySessionId(Long sessionId);
}