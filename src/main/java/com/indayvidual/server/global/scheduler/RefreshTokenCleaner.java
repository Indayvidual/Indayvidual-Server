package com.indayvidual.server.global.scheduler;

import com.indayvidual.server.domain.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleaner {

    private final RefreshTokenRepository repo;

    @Scheduled(cron = "0 0 3 * * ?") // 매일 03:00
    public void clean() {
        repo.deleteByExpiredAtBefore(LocalDateTime.now());
    }
}
