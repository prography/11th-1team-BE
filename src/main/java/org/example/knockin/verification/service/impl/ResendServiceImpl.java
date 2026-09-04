package org.example.knockin.verification.service.impl;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import lombok.RequiredArgsConstructor;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.EmailErrorCode;
import org.example.knockin.verification.service.MailSendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResendServiceImpl implements MailSendService {
    @Value("${EMAIL_FROM:KnockIn <admin@knock-in.com>}")
    private String from;

    private final Resend resend;
    private final Webhook webhook;
    private final ObjectMapper objectMapper;

    @Override
    public Object mailSend(String to, String subject, String message) {
        try {
            CreateEmailOptions params = CreateEmailOptions.builder().from(from).to(to).subject(subject).html(message).build();
            CreateEmailResponse response = resend.emails().send(params);
            return response.getId();
        } catch (Exception e) {
            throw new BusinessException(EmailErrorCode.EMAIL_SEND_ERROR);
        }
    }

    @Override
    public Map<String, Object> mailWebHook(Object payload, Map<String, String> headers) {
        String svixId = headers.get("svix-id");
        String svixTimestamp = headers.get("svix-timestamp");
        String svixSignature = headers.get("svix-signature");

        HashMap<String, List<String>> headerMap = new HashMap<>();
        headerMap.put("svix-id", List.of(svixId));
        headerMap.put("svix-timestamp", List.of(svixTimestamp));
        headerMap.put("svix-signature", List.of(svixSignature));
        HttpHeaders httpHeaders = HttpHeaders.of(headerMap, (key, value) -> true);

        try {
            webhook.verify((String) payload, httpHeaders);

            Map<String, Object> event = objectMapper.readValue((String) payload, Map.class);
            String eventType = (String) event.get("type");

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("eventType", eventType);
            resultMap.put("data", event.get("data"));
            return resultMap;
        } catch (WebhookVerificationException e) {
            throw new BusinessException(EmailErrorCode.EMAIL_WEBHOOK_ERROR);
        }
    }
}
