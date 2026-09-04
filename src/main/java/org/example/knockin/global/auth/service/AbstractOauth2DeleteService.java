package org.example.knockin.global.auth.service;

import org.example.knockin.verification.entity.LoginProviderType;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractOauth2DeleteService implements Oauth2DeleteService{
    protected final RestTemplate restTemplate;

    protected AbstractOauth2DeleteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public abstract boolean requestUnlink(String providerId);

    public abstract LoginProviderType getProviderType();
}
