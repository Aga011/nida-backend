package com.Aga.Agali.client;

import com.Aga.Agali.dto.ExamPaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentServiceClient {

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    private final WebClient webClient;

    public PaymentServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    public boolean payForExam(String teacherEmail, double amount) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("childEmail", teacherEmail);
            body.put("amount", amount);
            body.put("description", "Sinaq imtahanı ödənişi");

            webClient.post()
                    .uri(paymentServiceUrl + "/api/payment/pay")
                    .header("X-User-Email", teacherEmail)
                    .header("X-User-Role", "TEACHER")
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true;
        } catch (Exception e) {
            System.err.println("Ödəniş xətası: " + e.getMessage());
            return false;
        }
    }
}