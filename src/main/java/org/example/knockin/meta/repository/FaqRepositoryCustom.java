package org.example.knockin.meta.repository;

import org.example.knockin.meta.dto.FaqAllListDto;
import org.example.knockin.meta.dto.FaqListDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FaqRepositoryCustom {
    List<FaqListDto.Response.FaqInfo> findFaqList(Pageable pageable);
    List<FaqAllListDto.Response.FaqInfo> findFaqAllList(Pageable pageable);
}
