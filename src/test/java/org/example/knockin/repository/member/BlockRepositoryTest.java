package org.example.knockin.repository.member;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.example.knockin.global.config.QueryDslConfig;
import org.example.knockin.member.dto.BlockListDto;
import org.example.knockin.verification.entity.LoginProviderType;
import org.example.knockin.member.entity.BasicInformation;
import org.example.knockin.member.entity.Block;
import org.example.knockin.member.entity.Gender;
import org.example.knockin.member.entity.Member;
import org.example.knockin.member.entity.MemberRole;
import org.example.knockin.member.repository.BlockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@DisplayName("차단 리포지토리")
class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("내 차단 목록은 차단 대상의 최신 이름과 차단 시각을 최신순으로 한 번에 조회한다")
    void findMyListReturnsLatestBasicInformationInBlockedOrder() {
        // given
        Member blocker = persistMember("blocker");
        Member firstBlocked = persistMember("first-blocked");
        Member secondBlocked = persistMember("second-blocked");
        Member otherBlocker = persistMember("other-blocker");

        persistBasicInformation(firstBlocked, "이전이름");
        persistBasicInformation(firstBlocked, "최신이름");
        persistBasicInformation(secondBlocked, "두번째회원");

        LocalDateTime olderBlockedAt = LocalDateTime.of(2026, 7, 24, 12, 0);
        LocalDateTime newerBlockedAt = LocalDateTime.of(2026, 7, 25, 12, 0);
        Block olderBlock = persistBlock(blocker, firstBlocked);
        Block newerBlock = persistBlock(blocker, secondBlocked);
        Block otherBlock = persistBlock(otherBlocker, firstBlocked);
        persistBlock(blocker, firstBlocked, true);

        entityManager.flush();
        updateCreatedAt(olderBlock.getId(), olderBlockedAt);
        updateCreatedAt(newerBlock.getId(), newerBlockedAt);
        updateCreatedAt(otherBlock.getId(), newerBlockedAt.plusHours(1));
        entityManager.clear();

        // when
        List<BlockListDto.Response.Block> result = blockRepository.findMyList(blocker.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(
                        BlockListDto.Response.Block::getUserId,
                        BlockListDto.Response.Block::getName,
                        BlockListDto.Response.Block::getCreateAt
                )
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(
                                secondBlocked.getId(),
                                "두번째회원",
                                newerBlockedAt
                        ),
                        org.assertj.core.groups.Tuple.tuple(
                                firstBlocked.getId(),
                                "최신이름",
                                olderBlockedAt
                        )
                );
    }

    @Test
    @DisplayName("두 회원 중 어느 한쪽이라도 활성 차단하면 양방향 차단 관계로 조회한다")
    void existsActiveBlockBetweenMembersChecksBothDirectionsAndIgnoresDeletedBlocks() {
        // Given
        Member first = persistMember("first-member");
        Member second = persistMember("second-member");
        Member third = persistMember("third-member");
        Member fourth = persistMember("fourth-member");
        persistBlock(first, second);
        persistBlock(third, fourth, true);
        entityManager.flush();
        entityManager.clear();

        // When & Then
        assertThat(blockRepository.existsActiveBlockBetweenMembers(first.getId(), second.getId())).isTrue();
        assertThat(blockRepository.existsActiveBlockBetweenMembers(second.getId(), first.getId())).isTrue();
        assertThat(blockRepository.existsActiveBlockBetweenMembers(third.getId(), fourth.getId())).isFalse();
        assertThat(blockRepository.existsActiveBlockBetweenMembers(first.getId(), fourth.getId())).isFalse();
    }

    private Member persistMember(String providerId) {
        Member member = Member.builder()
                .providerType(LoginProviderType.KAKAO)
                .providerId(providerId)
                .role(MemberRole.USER)
                .isDelete(false)
                .build();
        entityManager.persist(member);
        return member;
    }

    private void persistBasicInformation(Member member, String name) {
        BasicInformation basicInformation = BasicInformation.builder()
                .member(member)
                .name(name)
                .birth(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .email(name + "@example.com")
                .build();
        entityManager.persist(basicInformation);
    }

    private Block persistBlock(Member blocker, Member blocked) {
        return persistBlock(blocker, blocked, false);
    }

    private Block persistBlock(Member blocker, Member blocked, boolean isDeleted) {
        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .isDeleted(isDeleted)
                .build();
        entityManager.persist(block);
        return block;
    }

    private void updateCreatedAt(Long blockId, LocalDateTime createdAt) {
        entityManager.createNativeQuery("update block set created_at = :createdAt where id = :blockId")
                .setParameter("createdAt", createdAt)
                .setParameter("blockId", blockId)
                .executeUpdate();
    }
}
