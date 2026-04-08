package com.Aga.Agali.repository;

import com.Aga.Agali.entity.GradeLevel;
import com.Aga.Agali.entity.Subject;
import com.Aga.Agali.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findBySubjectAndGradeLevelOrderByOrderIndex(Subject subject, GradeLevel gradeLevel);
    List<Topic> findByGradeLevelOrderByOrderIndex(GradeLevel gradeLevel);
}