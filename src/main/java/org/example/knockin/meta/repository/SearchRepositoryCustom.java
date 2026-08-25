package org.example.knockin.meta.repository;

import org.example.knockin.meta.dto.PopularSearchDto;

import java.util.List;

public interface SearchRepositoryCustom {
    List<PopularSearchDto.Response.RankItem> findPopSearch();
}