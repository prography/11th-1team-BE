package org.example.knockin.global.auth.util;

import java.security.Principal;
import org.example.knockin.global.auth.dto.PrincipalDetails;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class PrincipalMemberResolver {

    public Long resolveMemberId(Principal principal) {
        return resolvePrincipalDetails(principal).getMember().getId();
    }

    public PrincipalDetails resolvePrincipalDetails(Principal principal) {
        if (!(principal instanceof Authentication authentication)
                || !(authentication.getPrincipal() instanceof PrincipalDetails details)) {
            throw new AuthException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        return details;
    }
}
