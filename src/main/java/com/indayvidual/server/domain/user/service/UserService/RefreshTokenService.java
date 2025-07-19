package com.indayvidual.server.domain.user.service.UserService;

import com.indayvidual.server.domain.user.dto.response.LoginResponseDTO;
import com.indayvidual.server.domain.user.entity.RefreshToken;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.repository.RefreshTokenRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.config.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final JwtTokenProvider jwt;
    private final UserRepository userRepo;

    /** 로그인 시 호출 → 서버에 RefreshToken 저장 */
    public String saveRefreshToken(User user) {
        String refresh = jwt.generateRefreshToken(user);
        Claims claims = jwt.parseClaims(refresh); // jti, exp …

        repo.save(RefreshToken.of(
                user.getId(),
                claims.getId(),
                refresh,
                Duration.between(Instant.now(), claims.getExpiration().toInstant())
        ));
        return refresh;
    }

    /** /api/auth/refresh 호출 */
    public LoginResponseDTO rotate(String oldRefresh) {
        Claims claims = jwt.parseClaims(oldRefresh);
        log.info("claims.getId() = {}", claims.getId());
        log.info("claims = {}", claims);  // 전체 claims 로그
        log.info("추출된 tokenId(jti): {}", claims.getId());
        RefreshToken stored = repo.findByTokenId(claims.getId())
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 refreshToken"));

        if (!stored.match(oldRefresh) || stored.isRevoked() || stored.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("만료되었거나 취소된 refreshToken");
        }

        // 1) 현재 refreshToken 사용 종료
        stored.revoke();

        // 2) 새 토큰 발급
        User user = userRepo.findById(Long.parseLong(claims.getSubject()))
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저"));

        String newAccess = jwt.generateAccessToken(user);
        String newRefresh = saveRefreshToken(user); // 위 메서드 재사용

        return LoginResponseDTO.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}

