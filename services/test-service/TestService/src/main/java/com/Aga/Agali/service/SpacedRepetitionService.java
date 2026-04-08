package com.Aga.Agali.service;

import com.Aga.Agali.entity.QuestionAttempt;
import com.Aga.Agali.repository.QuestionAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpacedRepetitionService {

    private final QuestionAttemptRepository attemptRepository;

    public List<QuestionAttempt> getDueForReview(String userEmail) {
        return attemptRepository
                .findByUserEmailAndNextReviewDateBeforeAndCorrectFalse(
                        userEmail,
                        LocalDateTime.now());
    }

    public LocalDateTime calculateNextReview(int attemptCount, boolean correct) {
        if (!correct) {
            return LocalDateTime.now().plusDays(7);
        }

        return switch (attemptCount) {
            case 1 -> LocalDateTime.now().plusDays(1);
            case 2 -> LocalDateTime.now().plusDays(3);
            case 3 -> LocalDateTime.now().plusDays(7);
            case 4 -> LocalDateTime.now().plusDays(14);
            default -> LocalDateTime.now().plusDays(30);
        };
    }

    public void updateReviewDate(QuestionAttempt attempt, boolean correct) {
        attempt.setAttemptCount(attempt.getAttemptCount() + 1);
        attempt.setNextReviewDate(
                calculateNextReview(attempt.getAttemptCount(), correct));
        attemptRepository.save(attempt);
    }
}