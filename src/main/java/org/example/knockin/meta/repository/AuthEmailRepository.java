package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.AuthEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthEmailRepository extends JpaRepository<AuthEmail, Long> {
    List<AuthEmail> findByIsDeletedFalse();
    Optional<AuthEmail> findByIdAndIsDeletedFalse(Long id);
    boolean existsByDomainAndIsDeletedFalse(String domain);
    boolean existsByDomainAndIsDeletedFalseAndIdNot(String domain, Long id);
}
