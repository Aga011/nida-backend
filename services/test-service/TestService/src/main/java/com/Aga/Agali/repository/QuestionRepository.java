package com.Aga.Agali.repository;

import com.Aga.Agali.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySubjectAndGradeLevel(Subject subject, GradeLevel gradeLevel);
    List<Question> findByGradeLevel(GradeLevel gradeLevel);
    List<Question> findByInAssessmentTrue();
}