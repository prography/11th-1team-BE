package org.example.knockin.meta.service;

import lombok.RequiredArgsConstructor;
import org.example.knockin.meta.entity.AlarmSettingType;
import org.example.knockin.member.entity.Member;
import org.example.knockin.meta.repository.AlarmSettingRepository;
import org.example.knockin.meta.service.impl.FcmServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl {
    private final AlarmSettingRepository alarmSettingRepository;
    private final FcmServiceImpl fcmService;

    public void send(Member receiver, AlarmSettingType alarmSettingType, String title, String body, String deepLink) {
        String fcmToken = receiver.getFcmToken();
        if (!StringUtils.hasText(fcmToken)) return;
        boolean isEnabled = alarmSettingRepository.existsByMemberAndAlarmSettingTypeAndIsEnabledTrue(receiver, alarmSettingType);
        if (!isEnabled) return;
        fcmService.send(title, body, fcmToken, deepLink);
    }
}
