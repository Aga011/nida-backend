package com.Aga.Agali.repository;

import com.Aga.Agali.entity.ExamComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamCommentRepository extends JpaRepository<ExamComment, Long> {
    Optional<ExamComment> findByExamSessionId(Long examSessionId);
}