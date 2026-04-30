package org.example.infratest.repository;

import org.example.infratest.entity.MemberEntity;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberEntity> searchMembers(String username);
}
