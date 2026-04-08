package com.Aga.Agali.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TestServiceClient {

    @Value("${test.service.url}")
    private String testServiceUrl;

    private final WebClient webClient;

    public TestServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public void startAssessment(String userEmail, String gradeLevel) {
        webClient.post()
                .uri(testServiceUrl + "/api/test/assessment/start")
                .header("X-User-Email", userEmail)
                .header("X-User-GradeLevel", gradeLevel)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}