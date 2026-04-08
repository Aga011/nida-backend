package com.Aga.Agali.repository;

import com.Aga.Agali.entity.Exam;
import com.Aga.Agali.entity.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByTeacherEmail(String teacherEmail);
    List<Exam> findByGroupId(Long groupId);
    List<Exam> findByStatus(ExamStatus status);
    List<Exam> findByGroupIdAndStatus(Long groupId, ExamStatus status);
}