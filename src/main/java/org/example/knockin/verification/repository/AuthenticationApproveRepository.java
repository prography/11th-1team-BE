package org.example.knockin.verification.repository;

import org.example.knockin.verification.entity.Authentication;
import org.example.knockin.verification.entity.AuthenticationApprove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationApproveRepository extends JpaRepository<AuthenticationApprove, Long> {
    Optional<AuthenticationApprove> findByAuthentication(Authentication authentication);
}