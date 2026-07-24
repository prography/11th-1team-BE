package org.example.knockin.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.dto.AlarmListDto;
import org.example.knockin.dto.AlarmReadAllDto;
import org.example.knockin.dto.AlarmReadDto;
import org.example.knockin.dto.AlarmSendDto;
import org.example.knockin.entity.alarm.Alarm;
import org.example.knockin.entity.member.Member;
import org.example.knockin.exception.AuthErrorCode;
import org.example.knockin.exception.AlarmErrorCode;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.repository.alarm.AlarmRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl {
    private final MemberServiceImpl memberService;
    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
    private final AlarmRepository alarmRepository;

    public SseEmitter subscribe(Long memberId) {
        memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        long timeout = 1000L * 60 * 60;
        SseEmitter sseEmitter = new SseEmitter(timeout);
        sseEmitterMap.put(memberId, sseEmitter);
        sseEmitter.onCompletion(() -> sseEmitterMap.remove(memberId));
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onError(throwable -> sseEmitter.complete());
        return sseEmitter;
    }

    @Transactional
    public void sendToClient(Long memberId, String eventName, Alarm alarm) {
        alarmRepository.save(alarm);
        SseEmitter sseEmitter = sseEmitterMap.get(memberId);
        // 앱이 연결되어 있지 않아도 알림 저장 자체는 성공해야 한다.
        if (sseEmitter == null) {
            return;
        }
        try {
            String eventId = memberId + "_" + System.currentTimeMillis();
            AlarmSendDto.Response response = AlarmSendDto.Response.builder()
                    .id(alarm.getId()).title(alarm.getTitle())
                    .event(AlarmSendDto.Response.Event.builder().alarmType(alarm.getType()).eventId(alarm.getId()).build())
                    .isRead(alarm.getIsRead()).contents(alarm.getContents())
                    .createdAt(alarm.getCreatedAt()).expiredAt(alarm.getExpiredAt()).build();

            sseEmitter.send(SseEmitter.event().id(eventId).name(eventName).data(response));
        } catch (IOException e) {
            sseEmitterMap.remove(memberId);
            // 끊긴 SSE 때문에 채팅 요청 같은 원래 작업까지 롤백시키지 않는다.
            sseEmitter.complete();
        }
    }

    public AlarmListDto.Response findAlarmList(Pageable pageable, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        List<AlarmListDto.Response.Alarm> alarmList = alarmRepository.findByMember(member, pageable).stream().map(item ->
                AlarmListDto.Response.Alarm.builder().id(item.getId()).title(item.getTitle()).contents(item.getContents()).isRead(item.getIsRead()).expiredAt(item.getExpiredAt()).createAt(item.getCreatedAt()).build()).toList();
        return AlarmListDto.Response.builder().alarms(alarmList).build();
    }

    @Transactional
    public AlarmReadDto.Response modifyAlarmRead(Long id, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        Alarm alarm = alarmRepository.findByIdAndMember(id, member).orElseThrow(() -> new BusinessException(AlarmErrorCode.ALARM_NOT_FOUND));;
        alarm.readAlarm();
        return AlarmReadDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public AlarmReadAllDto.Response modifyAllAlarmRead(Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        List<Alarm> alarmList = alarmRepository.findByMemberAndIsRead(member, false);
        alarmList.forEach(Alarm::readAlarm);
        return AlarmReadAllDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }
}
