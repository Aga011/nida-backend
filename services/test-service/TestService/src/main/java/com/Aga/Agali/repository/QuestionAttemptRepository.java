package com.Aga.Agali.repository;

import com.Aga.Agali.entity.QuestionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionAttemptRepository extends JpaRepository<QuestionAttempt, Long> {
    List<QuestionAttempt> findByUserEmailAndSessionId(String userEmail, Long sessionId);
    List<QuestionAttempt> findByUserEmailAndCorrectFalse(String userEmail);
    List<QuestionAttempt> findByUserEmailAndNextReviewDateBeforeAndCorrectFalse(
            String userEmail, LocalDateTime date);
    Optional<QuestionAttempt> findByUserEmailAndQuestionId(
            String userEmail, Long questionId);
}