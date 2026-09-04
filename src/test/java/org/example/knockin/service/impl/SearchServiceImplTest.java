package org.example.knockin.service.impl;
 
import org.example.knockin.meta.dto.PopularSearchDto;
import org.example.knockin.member.entity.Member;
import org.example.knockin.meta.entity.Search;
import org.example.knockin.meta.repository.SearchRepository;
import org.example.knockin.meta.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.List;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
 
@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {
 
    @Mock
    private SearchRepository searchRepository;
 
    @InjectMocks
    private SearchServiceImpl searchService;
 
    @Test
    @DisplayName("인기 검색어 조회 테스트")
    void findPopSearchTest() {
        PopularSearchDto.Response.RankItem rankItem = PopularSearchDto.Response.RankItem.builder().keyword("검색어").build();
        given(searchRepository.findPopSearch()).willReturn(List.of(rankItem));
 
        List<PopularSearchDto.Response.RankItem> result = searchService.findPopSearch();
 
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getKeyword()).isEqualTo("검색어");
        verify(searchRepository).findPopSearch();
    }

    @Test
    @DisplayName("회원의 검색 키워드는 앞뒤 공백을 제거해 저장한다")
    void saveSearchKeywordTest() {
        // Given
        Member member = mock(Member.class);
        given(searchRepository.save(any(Search.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        Search result = searchService.save(member, "  원룸  ");

        // Then
        ArgumentCaptor<Search> searchCaptor = ArgumentCaptor.forClass(Search.class);
        verify(searchRepository).save(searchCaptor.capture());
        assertThat(searchCaptor.getValue()).isSameAs(result);
        assertThat(result.getMember()).isSameAs(member);
        assertThat(result.getKeyword()).isEqualTo("원룸");
    }
}
