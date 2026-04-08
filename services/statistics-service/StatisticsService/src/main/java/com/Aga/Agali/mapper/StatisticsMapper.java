package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.SubjectStat;
import com.Aga.Agali.entity.TestResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatisticsMapper {

    public SubjectStatDto toSubjectStatDto(SubjectStat stat) {
        return SubjectStatDto.builder()
                .subject(stat.getSubject())
                .totalAnswered(stat.getTotalAnswered())
                .correctAnswers(stat.getCorrectAnswers())
                .successRate(stat.getSuccessRate())
                .level(stat.getLevel())
                .totalTimeSeconds(stat.getTotalTimeSeconds())
                .avgTimePerQuestion(stat.getAvgTimePerQuestion())
                .build();
    }

    public ProgressDto toProgressDto(TestResult result) {
        return ProgressDto.builder()
                .date(result.getCreatedAt())
                .percentage(result.getPercentage())
                .totalQuestions(result.getTotalQuestions())
                .correctAnswers(result.getCorrectAnswers())
                .timeSpentSeconds(result.getTotalTimeSeconds())
                .sessionType(result.getSessionType())
                .build();
    }

    public StatisticsResponse toStatisticsResponse(
            String userEmail,
            int totalTests,
            double averagePercentage,
            Long totalTimeSeconds,
            List<SubjectStat> subjectStats,
            List<ProgressDto> weeklyProgress,
            List<ProgressDto> monthlyProgress,
            List<SuspiciousActivityDto> suspiciousActivities) {

        Map<String, SubjectStatDto> subjectStatMap = subjectStats.stream()
                .collect(Collectors.toMap(
                        SubjectStat::getSubject,
                        this::toSubjectStatDto));

        List<String> weakSubjects = subjectStats.stream()
                .filter(s -> "WEAK".equals(s.getLevel()))
                .map(SubjectStat::getSubject)
                .toList();

        List<String> strongSubjects = subjectStats.stream()
                .filter(s -> "STRONG".equals(s.getLevel()))
                .map(SubjectStat::getSubject)
                .toList();

        return StatisticsResponse.builder()
                .userEmail(userEmail)
                .totalTests(totalTests)
                .averagePercentage(averagePercentage)
                .totalTimeSeconds(totalTimeSeconds)
                .subjectStats(subjectStatMap)
                .weakSubjects(weakSubjects)
                .strongSubjects(strongSubjects)
                .weeklyProgress(weeklyProgress)
                .monthlyProgress(monthlyProgress)
                .suspiciousActivities(suspiciousActivities)
                .build();
    }
}