package org.example.knockin.member.repository;

import java.util.List;
import org.example.knockin.member.dto.BlockListDto.Response.Block;

public interface BlockRepositoryCustom {
    List<Block> findMyList(Long blockerId);

    boolean existsActiveBlockBetweenMembers(Long firstMemberId, Long secondMemberId);
}
