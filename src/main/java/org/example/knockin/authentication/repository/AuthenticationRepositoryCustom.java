package org.example.knockin.authentication.repository;

import java.util.List;

import org.example.knockin.authentication.dto.BoVerificationApproveListDto;
import org.example.knockin.authentication.dto.BoVerificationCancelListDto;
import org.example.knockin.authentication.dto.BoVerificationWaitingDetailDto;
import org.example.knockin.authentication.dto.BoVerificationWaitingListDto;
import org.example.knockin.dto.*;
import org.example.knockin.authentication.entity.AuthenticationType;
import org.example.knockin.member.entity.Member;
import org.example.knockin.authentication.repository.row.MemberAuthenticationRow;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AuthenticationRepositoryCustom {
    List<AuthenticationType> getAcceptedAuthenticationTypeByMemberId(Long memberId);

    List<MemberAuthenticationRow> findAcceptedByMemberIds(List<Long> memberIds);

    List<BoVerificationApproveListDto.Response.EmployeeAuthItem> findVerificationApproves(Pageable pageable);

    List<BoVerificationCancelListDto.Response.EmployeeAuthItem> findVerificationCancels(Pageable pageable);

    List<BoVerificationWaitingListDto.Response.EmployeeAuthItem> findVerificationsList(Pageable pageable);

    BoVerificationWaitingDetailDto.Response findVerifications(Long id);

    Optional<MyVerificationListDto.Response.AuthInfo> findVerificationList(Pageable pageable, Member member, AuthenticationType authenticationType);
}
