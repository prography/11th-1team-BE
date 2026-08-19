package org.example.knockin.dto;

import org.example.knockin.entity.auth.LoginProviderType;

public interface OAuth2UserInfo {
    String getId();
    String getName();
    String getEmail();
    LoginProviderType getProviderType();
}
