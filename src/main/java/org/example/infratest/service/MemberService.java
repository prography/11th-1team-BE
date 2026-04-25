package org.example.infratest.service;

import lombok.RequiredArgsConstructor;
import org.example.infratest.entity.MemberEntity;
import org.example.infratest.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberEntity save(String name) {
        return memberRepository.save(MemberEntity.builder().name(name).build());
    }

    public List<MemberEntity> list() {
        return memberRepository.findAll();
    }
}
