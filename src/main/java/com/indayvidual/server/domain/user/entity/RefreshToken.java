package com.indayvidual.server.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(indexes = @Index(columnList = "tokenId"))
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String tokenId;
    private String hashedToken;
    private LocalDateTime expiredAt;
    private boolean revoked = false;

    public static RefreshToken of(Long userId,
                                  String tokenId,
                                  String rawToken,
                                  Duration ttl) {
        return new RefreshToken(
                null,
                userId,
                tokenId,
                hash(rawToken),
                LocalDateTime.now().plus(ttl),
                false
        );
    }

    // 토큰 일치 여부 (만료시간 + revoked 상태 체크)
    public boolean match(String rawToken) {
        return !revoked
                && !isExpired()
                && Objects.equals(this.hashedToken, hash(rawToken));
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }

    public void revoke() {
        this.revoked = true;
    }

    private static String hash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
