package com.Aga.Agali.consumer;

import com.Aga.Agali.dto.TestResultDto;
import com.Aga.Agali.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestResultConsumer {

    private final StatisticsService statisticsService;

    @KafkaListener(topics = "test-results", groupId = "statistics-group")
    public void consume(TestResultDto testResultDto) {
        statisticsService.processTestResult(testResultDto);
    }
}
