package com.Aga.Agali.service;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.mapper.StatisticsMapper;
import com.Aga.Agali.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TestResultRepository testResultRepository;
    private final SubjectStatRepository subjectStatRepository;
    private final StatisticsMapper mapper;
    private final ParentResultService parentResultService;
    private final RabbitTemplate rabbitTemplate;

    private static final long SUSPICIOUS_TIME_THRESHOLD = 3L;
    public void processTestResult(TestResultDto dto) {
        if (testResultRepository.existsBySessionId(dto.getSessionId())) {
            return;
        }

        TestResult result = TestResult.builder()
                .sessionId(dto.getSessionId())
                .userEmail(dto.getUserEmail())
                .gradeLevel(dto.getGradeLevel())
                .specialtyGroup(dto.getSpecialtyGroup())
                .sessionType(dto.getSessionType())
                .totalQuestions(dto.getTotalQuestions())
                .correctAnswers(dto.getCorrectAnswers())
                .percentage(dto.getPercentage())
                .totalTimeSeconds(dto.getTotalTimeSeconds())
                .build();

        testResultRepository.save(result);
        updateSubjectStats(dto.getUserEmail(),
                dto.getSubjectResults(),
                dto.getSubjectTimeResults());


        parentResultService.createParentResult(dto);


        checkSuspiciousActivity(dto);
    }
    private void updateSubjectStats(String userEmail,
                                    Map<String, Integer> subjectResults,
                                    Map<String, Long> subjectTimeResults) {
        if (subjectResults == null) return;

        subjectResults.forEach((subject, correct) -> {
            SubjectStat stat = subjectStatRepository
                    .findByUserEmailAndSubject(userEmail, subject)
                    .orElse(SubjectStat.builder()
                            .userEmail(userEmail)
                            .subject(subject)
                            .totalAnswered(0)
                            .correctAnswers(0)
                            .totalTimeSeconds(0L)
                            .build());

            int questionCount = subjectResults.getOrDefault(subject, 0);
            long timeSpent = subjectTimeResults != null
                    ? subjectTimeResults.getOrDefault(subject, 0L) : 0L;

            stat.setTotalAnswered(stat.getTotalAnswered() + questionCount);
            stat.setCorrectAnswers(stat.getCorrectAnswers() + correct);
            stat.setTotalTimeSeconds(stat.getTotalTimeSeconds() + timeSpent);

            double successRate = (double) stat.getCorrectAnswers()
                    / stat.getTotalAnswered() * 100;
            double avgTime = stat.getTotalAnswered() > 0
                    ? (double) stat.getTotalTimeSeconds() / stat.getTotalAnswered() : 0;

            stat.setSuccessRate(successRate);
            stat.setAvgTimePerQuestion(avgTime);
            stat.setLevel(calculateLevel(successRate));

            subjectStatRepository.save(stat);
        });
    }
    private void checkSuspiciousActivity(TestResultDto dto) {
        if (dto.getTotalQuestions() == 0 || dto.getTotalTimeSeconds() == null) {
            return;
        }

        double avgTime = (double) dto.getTotalTimeSeconds() / dto.getTotalQuestions();

        if (avgTime < SUSPICIOUS_TIME_THRESHOLD) {

            NotificationDto notification = NotificationDto.builder()
                    .senderEmail("system@platform.com")
                    .receiverEmail(dto.getUserEmail())
                    .message("Şagird " + dto.getUserEmail()
                            + " sual başına ortalama "
                            + String.format("%.1f", avgTime)
                            + " saniyə sərf etdi — şübhəli davranış aşkar edildi!")
                    .type("WEAK_SUBJECT_ALERT")
                    .build();

            rabbitTemplate.convertAndSend(
                    "notification-exchange",
                    "notification-routing-key",
                    notification);
        }
    }

    private String calculateLevel(double successRate) {
        if (successRate >= 70) return "STRONG";
        if (successRate >= 40) return "MEDIUM";
        return "WEAK";
    }

    public StatisticsResponse getStatistics(String userEmail) {
        List<TestResult> results = testResultRepository.findByUserEmail(userEmail);
        List<SubjectStat> subjectStats =
                subjectStatRepository.findByUserEmail(userEmail);

        int totalTests = results.size();
        double averagePercentage = results.stream()
                .mapToDouble(TestResult::getPercentage)
                .average()
                .orElse(0.0);
        long totalTime = results.stream()
                .mapToLong(r -> r.getTotalTimeSeconds() != null
                        ? r.getTotalTimeSeconds() : 0L)
                .sum();

        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        List<ProgressDto> weeklyProgress = testResultRepository
                .findByUserEmailAndCreatedAtBetween(userEmail, weekAgo, LocalDateTime.now())
                .stream()
                .map(mapper::toProgressDto)
                .toList();


        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        List<ProgressDto> monthlyProgress = testResultRepository
                .findByUserEmailAndCreatedAtBetween(userEmail, monthAgo, LocalDateTime.now())
                .stream()
                .map(mapper::toProgressDto)
                .toList();

        List<SuspiciousActivityDto> suspicious =
                detectSuspiciousActivity(userEmail, results);

        return mapper.toStatisticsResponse(
                userEmail, totalTests, averagePercentage,
                totalTime, subjectStats,
                weeklyProgress, monthlyProgress, suspicious);
    }

    private List<SuspiciousActivityDto> detectSuspiciousActivity(
            String userEmail, List<TestResult> results) {

        List<SuspiciousActivityDto> suspicious = new ArrayList<>();

        for (TestResult result : results) {
            if (result.getTotalQuestions() > 0
                    && result.getTotalTimeSeconds() != null) {
                double avgTime = (double) result.getTotalTimeSeconds()
                        / result.getTotalQuestions();

                if (avgTime < SUSPICIOUS_TIME_THRESHOLD) {
                    suspicious.add(SuspiciousActivityDto.builder()
                            .userEmail(userEmail)
                            .questionId(result.getSessionId())
                            .timeSpentSeconds(result.getTotalTimeSeconds())
                            .detectedAt(result.getCreatedAt())
                            .reason("Sual başına ortalama vaxt "
                                    + String.format("%.1f", avgTime)
                                    + " saniyədir — şübhəlidir")
                            .build());
                }
            }
        }
        return suspicious;
    }
}