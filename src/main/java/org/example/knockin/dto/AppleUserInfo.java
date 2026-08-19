package org.example.knockin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.knockin.entity.auth.LoginProviderType;

public class AppleUserInfo implements OAuth2UserInfo {
    public static final String NAME_PARAMETER = "apple_user_name";
    public static final String EMAIL_PARAMETER = "apple_user_email";

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("is_private_email")
    private Boolean isPrivateEmail;

    @Override
    public String getId() {
        return sub;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void merge(String name, String email) {
        if (!hasText(this.name) && hasText(name)) {
            this.name = name.trim();
        }
        if (!hasText(this.email) && hasText(email)) {
            this.email = email.trim();
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public LoginProviderType getProviderType() {
        return LoginProviderType.APPLE;
    }
}