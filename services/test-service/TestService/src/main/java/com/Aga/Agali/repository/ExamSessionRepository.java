package com.Aga.Agali.repository;

import com.Aga.Agali.entity.ExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSessionRepository extends JpaRepository<ExamSession, Long> {
    List<ExamSession> findByExamId(Long examId);
    List<ExamSession> findByStudentEmail(String studentEmail);
    Optional<ExamSession> findByExamIdAndStudentEmail(
            Long examId, String studentEmail);
}