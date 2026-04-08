package com.Aga.Agali.consumer;

import com.Aga.Agali.dto.TestResultDto;
import com.Aga.Agali.service.LearningPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestResultConsumer {

    private final LearningPlanService learningPlanService;

    @KafkaListener(topics = "test-results", groupId = "learning-plan-group")
    public void consume(TestResultDto testResultDto) {
        learningPlanService.processTestResult(testResultDto);
    }
}