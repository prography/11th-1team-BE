package org.example.knockin.global.auth.dto;

import org.example.knockin.verification.entity.LoginProviderType;

public interface OAuth2UserInfo {
    String getId();
    String getEmail();
    LoginProviderType getProviderType();
    String getName();
}
