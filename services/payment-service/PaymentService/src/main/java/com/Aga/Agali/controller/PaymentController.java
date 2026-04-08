package com.Aga.Agali.controller;

import com.Aga.Agali.dto.*;
import com.Aga.Agali.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @RequestHeader("X-User-Email") String parentEmail,
            @RequestHeader("X-User-Role") String role) {
        if (!role.equals("PARENT") && !role.equals("TEACHER")) {
            throw new RuntimeException("İcazəniz yoxdur");
        }
        return ResponseEntity.ok(paymentService.getBalance(parentEmail));
    }
    @PostMapping("/topup")
    public ResponseEntity<BalanceResponse> topUp(
            @RequestHeader("X-User-Email") String parentEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid TopUpRequest request) {
        if (!role.equals("PARENT") && !role.equals("TEACHER")) {
            throw new RuntimeException("İcazəniz yoxdur");
        }
        return ResponseEntity.ok(paymentService.topUp(parentEmail, request));
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(
            @RequestHeader("X-User-Email") String parentEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid PaymentRequest request) {
        if (!role.equals("PARENT") && !role.equals("TEACHER")) {
            throw new RuntimeException("İcazəniz yoxdur");
        }
        return ResponseEntity.ok(
                paymentService.payMonthlyFee(parentEmail, request));
    }

    @PostMapping("/pay/multiple")
    public ResponseEntity<List<PaymentResponse>> payMultiple(
            @RequestHeader("X-User-Email") String parentEmail,
            @RequestHeader("X-User-Role") String role,
            @RequestBody @Valid List<PaymentRequest> requests) {
        if (!role.equals("PARENT")) {
            throw new RuntimeException("Yalnız valideynlər ödəniş edə bilər");
        }
        return ResponseEntity.ok(
                paymentService.payForMultipleChildren(parentEmail, requests));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> getHistory(
            @RequestHeader("X-User-Email") String parentEmail) {
        return ResponseEntity.ok(
                paymentService.getPaymentHistory(parentEmail));
    }

    @GetMapping("/history/child/{childEmail}")
    public ResponseEntity<List<PaymentResponse>> getChildHistory(
            @RequestHeader("X-User-Role") String role,
            @PathVariable String childEmail) {
        if (!role.equals("PARENT") && !role.equals("ADMIN")) {
            throw new RuntimeException("İcazəniz yoxdur");
        }
        return ResponseEntity.ok(
                paymentService.getChildPaymentHistory(childEmail));
    }
}