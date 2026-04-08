package com.Aga.Agali.repository;

import com.Aga.Agali.entity.Payment;
import com.Aga.Agali.entity.PaymentStatus;
import com.Aga.Agali.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByParentEmail(String parentEmail);
    List<Payment> findByChildEmail(String childEmail);
    List<Payment> findByParentEmailAndType(String parentEmail, PaymentType type);
    List<Payment> findByParentEmailAndStatus(String parentEmail, PaymentStatus status);
}