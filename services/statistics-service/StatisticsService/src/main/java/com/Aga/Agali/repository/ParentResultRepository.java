package com.Aga.Agali.repository;

import com.Aga.Agali.entity.ParentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParentResultRepository extends JpaRepository<ParentResult, Long> {
    List<ParentResult> findByStudentEmailAndVisibleTrue(String studentEmail);
    List<ParentResult> findByVisibleFalseAndVisibleAtBefore(LocalDateTime now);
}