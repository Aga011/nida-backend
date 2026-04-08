package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.TestResultDto;
import com.Aga.Agali.entity.TestSession;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestMapper {

    public TestResultDto toTestResultDto(TestSession session, Map<String, Integer> subjectResults) {
        double percentage = session.getTotalQuestions() > 0
                ? (double) session.getCorrectAnswers() / session.getTotalQuestions() * 100
                : 0;

        return TestResultDto.builder()
                .sessionId(session.getId())
                .userEmail(session.getUserEmail())
                .gradeLevel(session.getGradeLevel())
                .specialtyGroup(session.getSpecialtyGroup())
                .totalQuestions(session.getTotalQuestions())
                .correctAnswers(session.getCorrectAnswers())
                .percentage(percentage)
                .subjectResults(subjectResults)
                .build();
    }
}