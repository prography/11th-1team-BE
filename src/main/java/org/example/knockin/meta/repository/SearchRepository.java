package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryCustom {
}