package org.example.knockin.meta.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.knockin.meta.dto.PopularSearchDto;
import org.example.knockin.meta.repository.SearchRepositoryCustom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.knockin.meta.entity.QSearch.search;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Value("${popular.count.size}")
    private int popularCountSize;

    @Override
    public List<PopularSearchDto.Response.RankItem> findPopSearch() {
        return jpaQueryFactory
                .select(Projections.fields(PopularSearchDto.Response.RankItem.class,
                        search.keyword
                ))
                .from(search)
                .groupBy(search.keyword)
                .orderBy(search.count().desc())
                .limit(popularCountSize)
                .fetch();
    }
}