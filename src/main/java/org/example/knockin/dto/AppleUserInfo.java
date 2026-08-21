package org.example.knockin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.knockin.entity.auth.LoginProviderType;

public class AppleUserInfo implements OAuth2UserInfo {
    @JsonProperty("sub")
    private String sub;

    @JsonProperty("email")
    private String email;

    @JsonProperty("is_private_email")
    private Boolean isPrivateEmail;

    @JsonProperty("name")
    private String name;

    @Override
    public String getId() {
        return sub;
    }

    @Override
    public LoginProviderType getProviderType() {
        return LoginProviderType.APPLE;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }
}