package org.example.knockin.global.auth.service;

import org.example.knockin.verification.entity.LoginProviderType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Oauth2DeleteFactory {
    private final Map<LoginProviderType, Oauth2DeleteService> serviceMap;

    public Oauth2DeleteFactory(List<AbstractOauth2DeleteService> services) {
        this.serviceMap = services.stream().collect(Collectors.toMap(AbstractOauth2DeleteService::getProviderType, Function.identity()));
    }

    public Oauth2DeleteService getDeleteService(LoginProviderType providerType) {
        Oauth2DeleteService service = serviceMap.get(providerType);
        if (service == null) throw new IllegalArgumentException("지원하지 않는 로그인 제공자입니다: " + providerType);

        return service;
    }
}
