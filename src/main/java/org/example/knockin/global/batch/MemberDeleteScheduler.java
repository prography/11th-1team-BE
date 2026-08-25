package org.example.knockin.global.batch;

import lombok.RequiredArgsConstructor;
import org.example.knockin.member.service.impl.MemberServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDeleteScheduler {
    private final MemberServiceImpl memberService;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void run() {
        memberService.hardDeleteMember();
    }
}
