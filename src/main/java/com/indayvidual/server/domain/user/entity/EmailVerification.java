package com.indayvidual.server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "email"),
        @Index(columnList = "expiredAt")
})
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;

    private LocalDateTime expiredAt;
    private boolean verified;

    public static EmailVerification create(String email, String code, Duration ttl) {
        EmailVerification ev = new EmailVerification();
        ev.email = email;
        ev.code = code;
        ev.expiredAt = LocalDateTime.now().plus(ttl);
        ev.verified = false;
        return ev;
    }

    public boolean isExpired() { return LocalDateTime.now().isAfter(expiredAt); }
    public void markVerified() { this.verified = true; }
}

