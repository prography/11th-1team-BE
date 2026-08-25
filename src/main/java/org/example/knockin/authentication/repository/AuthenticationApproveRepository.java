package org.example.knockin.authentication.repository;

import org.example.knockin.authentication.entity.Authentication;
import org.example.knockin.authentication.entity.AuthenticationApprove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationApproveRepository extends JpaRepository<AuthenticationApprove, Long> {
    Optional<AuthenticationApprove> findByAuthentication(Authentication authentication);
}