package com.Aga.Agali.repository;

import com.Aga.Agali.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUniqueId(String uniqueId);
    Optional<User> findByVerificationToken(String token);
    Boolean existsByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
}