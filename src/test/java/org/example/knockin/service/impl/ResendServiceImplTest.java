package org.example.knockin.service.impl;

import com.resend.Resend;
import com.resend.services.emails.Emails;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import org.example.knockin.verification.service.impl.ResendServiceImpl;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.EmailErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import tools.jackson.databind.ObjectMapper;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Resend 이메일 전송 및 웹훅 서비스 테스트")
class ResendServiceImplTest {

    @Mock
    private Resend resend;

    @Mock
    private Webhook webhook;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ResendServiceImpl resendService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(resendService, "from", "KnockIn <admin@knock-in.com>");
    }

    @Test
    @DisplayName("메일 발송 성공 테스트")
    void mailSendSuccessTest() throws Exception {
        // given
        String to = "test@example.com";
        String subject = "Test Subject";
        String message = "Test Message";
        String expectedEmailId = "email_id_123";

        Emails emails = mock(Emails.class);
        CreateEmailResponse response = mock(CreateEmailResponse.class);

        given(resend.emails()).willReturn(emails);
        given(emails.send(any(CreateEmailOptions.class))).willReturn(response);
        given(response.getId()).willReturn(expectedEmailId);

        // when
        Object result = resendService.mailSend(to, subject, message);

        // then
        assertThat(result).isEqualTo(expectedEmailId);
        verify(emails).send(any(CreateEmailOptions.class));
    }

    @Test
    @DisplayName("메일 발송 실패 시 BusinessException 발생 테스트")
    void mailSendFailureTest() throws Exception {
        // given
        String to = "test@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        Emails emails = mock(Emails.class);
        given(resend.emails()).willReturn(emails);
        given(emails.send(any(CreateEmailOptions.class))).willThrow(new RuntimeException("API Connection Error"));

        // when & then
        assertThatThrownBy(() -> resendService.mailSend(to, subject, message))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", EmailErrorCode.EMAIL_SEND_ERROR);
    }

    @Test
    @DisplayName("메일 웹훅 수신 검증 및 역직렬화 성공 테스트")
    void mailWebHookSuccessTest() throws Exception {
        // given
        String payload = "{\"type\":\"email.sent\",\"data\":{\"id\":\"email_id_123\"}}";
        Map<String, String> headers = new HashMap<>();
        headers.put("svix-id", "svix_id_val");
        headers.put("svix-timestamp", "svix_ts_val");
        headers.put("svix-signature", "svix_sig_val");

        Map<String, Object> mockEvent = new HashMap<>();
        mockEvent.put("type", "email.sent");
        mockEvent.put("data", Map.of("id", "email_id_123"));

        doNothing().when(webhook).verify(eq(payload), any(HttpHeaders.class));
        given(objectMapper.readValue(eq(payload), eq(Map.class))).willReturn(mockEvent);

        // when
        Map<String, Object> result = resendService.mailWebHook(payload, headers);

        // then
        assertThat(result).isNotNull();
        assertThat(result.get("eventType")).isEqualTo("email.sent");
        assertThat(result.get("data")).isEqualTo(mockEvent.get("data"));
        verify(webhook).verify(eq(payload), any(HttpHeaders.class));
        verify(objectMapper).readValue(eq(payload), eq(Map.class));
    }

    @Test
    @DisplayName("웹훅 검증 실패 시 BusinessException 발생 테스트")
    void mailWebHookVerificationFailureTest() throws WebhookVerificationException {
        // given
        String payload = "invalid_payload";
        Map<String, String> headers = new HashMap<>();
        headers.put("svix-id", "invalid_svix_id");
        headers.put("svix-timestamp", "invalid_svix_timestamp");
        headers.put("svix-signature", "invalid_svix_signature");

        doThrow(new WebhookVerificationException("Signature mismatch"))
                .when(webhook).verify(eq(payload), any(HttpHeaders.class));

        // when & then
        assertThatThrownBy(() -> resendService.mailWebHook(payload, headers))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", EmailErrorCode.EMAIL_WEBHOOK_ERROR);
    }
}
