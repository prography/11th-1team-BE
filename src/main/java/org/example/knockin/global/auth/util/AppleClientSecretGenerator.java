package org.example.knockin.global.auth.util;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class AppleClientSecretGenerator {
    @Value("${apple.audience}")
    private String APPLE_AUDIENCE;
    @Value("${apple.key.file.path}")
    private String KEY_FILE_PATH;
    @Value("${apple.team.id}")
    private String TEAM_ID;
    @Value("${apple.key.id}")
    private String KEY_ID;

    private static final String JWT_TYPE = "JWT";
    private static final String KEY_FACTORY_ALGORITHM = "EC";
    private static final String PEM_BEGIN_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String PEM_END_HEADER = "-----END PRIVATE KEY-----";
    private static final String REGEX_WHITESPACE = "\\s+";

    public String createClientSecret(String clientId) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + 1000L * 60 * 5);
            PrivateKey privateKey = getPrivateKey();

            return Jwts.builder()
                    .header()
                    .keyId(KEY_ID)
                    .type(JWT_TYPE)
                    .and()
                    .issuer(TEAM_ID)
                    .issuedAt(now)
                    .expiration(expiration)
                    .audience().add(APPLE_AUDIENCE).and()
                    .subject(clientId)
                    .signWith(privateKey, Jwts.SIG.ES256)
                    .compact();
        } catch (Exception e) {
            log.error("Apple client_secret JWT 생성 중 오류가 발생했습니다.", e);
            throw new AuthException(AuthErrorCode.APPLE_CLIENT_SECRET_MAKE_FAIL);
        }
    }

    private PrivateKey getPrivateKey() throws Exception {
        ClassPathResource resource = new ClassPathResource(KEY_FILE_PATH);
        try (InputStream is = resource.getInputStream()) {
            String content = new String(is.readAllBytes());
            String privateKeyPEM = content
                    .replace(PEM_BEGIN_HEADER, "")
                    .replace(PEM_END_HEADER, "")
                    .replaceAll(REGEX_WHITESPACE, "");

            byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        }
    }
}
