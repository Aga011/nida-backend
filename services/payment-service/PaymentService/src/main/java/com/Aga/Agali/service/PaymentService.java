package com.Aga.Agali.service;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.entity.*;
import com.Aga.Agali.mapper.PaymentMapper;
import com.Aga.Agali.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BalanceRepository balanceRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    private static final BigDecimal MONTHLY_FEE = new BigDecimal("9.99");

    public BalanceResponse getBalance(String parentEmail) {
        Balance balance = balanceRepository.findByParentEmail(parentEmail)
                .orElseGet(() -> balanceRepository.save(
                        Balance.builder()
                                .parentEmail(parentEmail)
                                .amount(BigDecimal.ZERO)
                                .build()));
        return mapper.toBalanceResponse(balance);
    }

    @Transactional
    public BalanceResponse topUp(String parentEmail, TopUpRequest request) {
        Balance balance = balanceRepository.findByParentEmail(parentEmail)
                .orElseGet(() -> Balance.builder()
                        .parentEmail(parentEmail)
                        .amount(BigDecimal.ZERO)
                        .build());

        balance.setAmount(balance.getAmount().add(request.getAmount()));
        balanceRepository.save(balance);

        Payment payment = Payment.builder()
                .parentEmail(parentEmail)
                .type(PaymentType.BALANCE_TOP_UP)
                .status(PaymentStatus.SUCCESS)
                .amount(request.getAmount())
                .description("Balans artırıldı: " + request.getAmount() + " AZN")
                .build();
        paymentRepository.save(payment);

        return mapper.toBalanceResponse(balance);
    }

    @Transactional
    public PaymentResponse payMonthlyFee(String parentEmail,
                                         PaymentRequest request) {
        Balance balance = balanceRepository.findByParentEmail(parentEmail)
                .orElseThrow(() -> new RuntimeException("Balans tapılmadı"));

        BigDecimal amount = request.getAmount() != null
                ? request.getAmount() : MONTHLY_FEE;

        if (balance.getAmount().compareTo(amount) < 0) {
            Payment failedPayment = Payment.builder()
                    .parentEmail(parentEmail)
                    .childEmail(request.getChildEmail())
                    .type(PaymentType.MONTHLY_SUBSCRIPTION)
                    .status(PaymentStatus.FAILED)
                    .amount(amount)
                    .description("Balans kifayət deyil")
                    .build();
            paymentRepository.save(failedPayment);
            throw new RuntimeException(
                    "Balansınız kifayət deyil. Cari balans: "
                            + balance.getAmount() + " AZN");
        }

        balance.setAmount(balance.getAmount().subtract(amount));
        balanceRepository.save(balance);

        Payment payment = Payment.builder()
                .parentEmail(parentEmail)
                .childEmail(request.getChildEmail())
                .type(PaymentType.MONTHLY_SUBSCRIPTION)
                .status(PaymentStatus.SUCCESS)
                .amount(amount)
                .description(request.getDescription() != null
                        ? request.getDescription()
                        : "Aylıq abunəlik ödənişi")
                .build();

        return mapper.toPaymentResponse(paymentRepository.save(payment));
    }

    @Transactional
    public List<PaymentResponse> payForMultipleChildren(String parentEmail,
                                                        List<PaymentRequest> requests) {
        Balance balance = balanceRepository.findByParentEmail(parentEmail)
                .orElseThrow(() -> new RuntimeException("Balans tapılmadı"));

        // Ümumi məbləği hesabla
        BigDecimal totalAmount = requests.stream()
                .map(r -> r.getAmount() != null ? r.getAmount() : MONTHLY_FEE)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (balance.getAmount().compareTo(totalAmount) < 0) {
            throw new RuntimeException(
                    "Balansınız kifayət deyil. Lazım olan: "
                            + totalAmount + " AZN, Cari balans: "
                            + balance.getAmount() + " AZN");
        }

        return requests.stream()
                .map(request -> payMonthlyFee(parentEmail, request))
                .toList();
    }

    public List<PaymentResponse> getPaymentHistory(String parentEmail) {
        return paymentRepository.findByParentEmail(parentEmail)
                .stream()
                .map(mapper::toPaymentResponse)
                .toList();
    }

    public List<PaymentResponse> getChildPaymentHistory(String childEmail) {
        return paymentRepository.findByChildEmail(childEmail)
                .stream()
                .map(mapper::toPaymentResponse)
                .toList();
    }
}