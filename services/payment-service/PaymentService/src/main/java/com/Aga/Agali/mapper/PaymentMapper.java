package com.Aga.Agali.mapper;

import com.Aga.Agali.dto.BalanceResponse;
import com.Aga.Agali.dto.PaymentResponse;
import com.Aga.Agali.entity.Balance;
import com.Aga.Agali.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public BalanceResponse toBalanceResponse(Balance balance) {
        return BalanceResponse.builder()
                .id(balance.getId())
                .parentEmail(balance.getParentEmail())
                .amount(balance.getAmount())
                .updatedAt(balance.getUpdatedAt())
                .build();
    }

    public PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .parentEmail(payment.getParentEmail())
                .childEmail(payment.getChildEmail())
                .type(payment.getType().name())
                .status(payment.getStatus().name())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}