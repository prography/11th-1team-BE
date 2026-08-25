package org.example.knockin.authentication.service;

import java.util.Map;

public interface MailSendService {
    Object mailSend(String to, String subject, String message);
    Map<String, Object> mailWebHook(Object payload, Map<String, String> headers);
}
