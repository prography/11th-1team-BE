package org.example.knockin.meta.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.knockin.meta.dto.FaqAllListDto;
import org.example.knockin.meta.dto.FaqListDto;
import org.example.knockin.meta.repository.FaqRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.knockin.meta.entity.QFaq.faq;

@Repository
@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FaqListDto.Response.FaqInfo> findFaqList(Pageable pageable) {
        return jpaQueryFactory.select(Projections.fields(FaqListDto.Response.FaqInfo.class,
                faq.id,
                faq.title,
                faq.sort
                )).from(faq).orderBy(faq.sort.asc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
    }

    @Override
    public List<FaqAllListDto.Response.FaqInfo> findFaqAllList(Pageable pageable) {
        return jpaQueryFactory.select(Projections.fields(FaqAllListDto.Response.FaqInfo.class,
                        faq.id,
                        faq.title,
                        faq.contents,
                        faq.sort,
                        faq.createdAt,
                        faq.updatedAt
                )).from(faq).orderBy(faq.sort.asc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
    }
}
