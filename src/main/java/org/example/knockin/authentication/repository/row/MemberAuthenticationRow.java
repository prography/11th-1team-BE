package org.example.knockin.authentication.repository.row;

import org.example.knockin.authentication.entity.AuthenticationType;

public record MemberAuthenticationRow(
        Long memberId,
        AuthenticationType type
) {
}
