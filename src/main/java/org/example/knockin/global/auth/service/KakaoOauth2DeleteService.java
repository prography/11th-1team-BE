package org.example.knockin.global.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.example.knockin.verification.entity.LoginProviderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KakaoOauth2DeleteService extends AbstractOauth2DeleteService{
    @Value("${kakao.admin.key}")
    private String kakaoAdminKey;

    public KakaoOauth2DeleteService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public boolean requestUnlink(String providerId) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoAdminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", providerId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(unlinkUrl, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            log.error("카카오 연결 끊기(Unlink) 실패. Provider ID: {}, 에러: {}", providerId, e.getMessage());
            return false;
        }
    }

    @Override
    public LoginProviderType getProviderType() {
        return LoginProviderType.KAKAO;
    }
}
