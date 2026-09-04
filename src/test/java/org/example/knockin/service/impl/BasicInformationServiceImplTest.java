package org.example.knockin.service.impl;
 
import org.example.knockin.member.entity.BasicInformation;
import org.example.knockin.member.entity.Gender;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.MemberErrorCode;
import org.example.knockin.member.repository.BasicInformationRepository;
import org.example.knockin.member.repository.row.ChattingRoomBasicInfoRow;
import org.example.knockin.member.service.impl.BasicInformationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.List;
import java.util.Optional;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("기본 정보 서비스")
class BasicInformationServiceImplTest {
 
    @Mock
    private BasicInformationRepository basicInformationRepository;
 
    @InjectMocks
    private BasicInformationServiceImpl basicInformationService;
 
    @Test
    @DisplayName("회원이 주어지면 해당 회원의 기본 정보 목록을 조회한다")
    void findByMemberTest() {
        // Given
        Member member = mock(Member.class);
        BasicInformation info = mock(BasicInformation.class);
        given(basicInformationRepository.findByMember(member)).willReturn(List.of(info));
 
        // When
        List<BasicInformation> result = basicInformationService.findByMember(member);
 
        // Then
        assertThat(result).hasSize(1);
        verify(basicInformationRepository).findByMember(member);
    }
 
    @Test
    @DisplayName("기본 정보가 주어지면 Repository에 저장하고 저장 결과를 반환한다")
    void saveTest() {
        // Given
        BasicInformation info = mock(BasicInformation.class);
        given(basicInformationRepository.save(any(BasicInformation.class))).willReturn(info);
 
        // When
        BasicInformation result = basicInformationService.save(info);
 
        // Then
        assertThat(result).isEqualTo(info);
        verify(basicInformationRepository).save(info);
    }

    @Test
    @DisplayName("회원 ID로 채팅방 기본 정보 행을 조회한다")
    void findChattingRoomBasicInfoRowByMemberIdReturnsRow() {
        // Given
        Long memberId = 1L;
        ChattingRoomBasicInfoRow row = new ChattingRoomBasicInfoRow(
                memberId,
                "홍길동",
                java.time.LocalDate.of(2000, 1, 1),
                Gender.MALE,
                "profile.jpg"
        );
        given(basicInformationRepository.findChattingRoomBasicInfoRow(memberId)).willReturn(Optional.of(row));

        // When
        ChattingRoomBasicInfoRow result = basicInformationService.findChattingRoomBasicInfoRowByMemberId(memberId);

        // Then
        assertThat(result).isSameAs(row);
        verify(basicInformationRepository).findChattingRoomBasicInfoRow(memberId);
    }

    @Test
    @DisplayName("회원 ID에 해당하는 채팅방 기본 정보가 없으면 기본 정보 없음 예외를 던진다")
    void findChattingRoomBasicInfoRowByMemberIdThrowsWhenRowDoesNotExist() {
        // Given
        Long memberId = 1L;
        given(basicInformationRepository.findChattingRoomBasicInfoRow(memberId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> basicInformationService.findChattingRoomBasicInfoRowByMemberId(memberId))
                .isInstanceOfSatisfying(BusinessException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.BASIC_INFO_NOT_FOUND));
        verify(basicInformationRepository).findChattingRoomBasicInfoRow(memberId);
    }
}
