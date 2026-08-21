package org.example.knockin.dto;

import org.example.knockin.entity.auth.LoginProviderType;

public interface OAuth2UserInfo {
    String getId();
    String getEmail();
    LoginProviderType getProviderType();
    String getName();
}
