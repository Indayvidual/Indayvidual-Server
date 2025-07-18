package com.indayvidual.server.global.scheduler;

import com.indayvidual.server.domain.user.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailVerificationCleaner {

    private final EmailVerificationRepository repo;

    /** “0 0 12 * * ?”  → 매일 12:00 PM KST */
    @Scheduled(cron = "0 0 12 * * ?")
    public void clean() {
        repo.deleteByExpiredAtBefore(LocalDateTime.now());
    }
}
