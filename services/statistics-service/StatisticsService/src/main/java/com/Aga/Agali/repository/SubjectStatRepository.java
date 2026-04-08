package com.Aga.Agali.repository;

import com.Aga.Agali.entity.SubjectStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectStatRepository extends JpaRepository <SubjectStat,Long> {
    List<SubjectStat> findByUserEmail(String userEmail);
    Optional<SubjectStat> findByUserEmailAndSubject(String userEmail, String subject);
}
