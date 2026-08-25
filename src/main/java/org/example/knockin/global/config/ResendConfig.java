package org.example.knockin.global.config;

import com.resend.Resend;
import com.svix.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConfig {
    @Value("${mail.resend.secret-key}")
    private String secretKey;

    @Value("${mail.resend.webhook.secret-key}")
    private String webHookSecretKey;

    @Bean
    public Resend resend() {
        return new Resend(secretKey);
    }

    @Bean
    public Webhook webhook() {
        return new Webhook(webHookSecretKey);
    }
}
