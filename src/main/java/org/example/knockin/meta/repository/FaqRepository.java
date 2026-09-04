package org.example.knockin.meta.repository;

import org.example.knockin.meta.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq,Long>, FaqRepositoryCustom {
    List<Faq> findBySort(Integer sort);
}
