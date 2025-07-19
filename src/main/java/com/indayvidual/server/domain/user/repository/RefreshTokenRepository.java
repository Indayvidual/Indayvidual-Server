package com.indayvidual.server.domain.user.repository;

import com.indayvidual.server.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenId(String tokenId);
    void deleteByExpiredAtBefore(LocalDateTime time);
}

