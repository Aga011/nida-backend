package com.Aga.Agali.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamPaymentRequest {
    private String childEmail;
    private double amount;
    private String description;

    public ExamPaymentRequest(String teacherEmail, double amount) {
        this.childEmail = teacherEmail;
        this.amount = amount;
        this.description = "Sinaq imtahanı ödənişi";
    }
}