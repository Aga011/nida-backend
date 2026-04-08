package com.Aga.Agali.repository;

import com.Aga.Agali.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findBySessionId(Long sessionId);
    List<Answer> findByUserEmail(String userEmail);
}
