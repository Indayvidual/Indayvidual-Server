package com.indayvidual.server.global.config.security;

import com.indayvidual.server.domain.user.entity.*;
import com.indayvidual.server.domain.user.entity.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String USER_ID = "userId";
    private static final String ROLE = "role";
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L; // 24시간
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    private SecretKey signingKey;

    @PostConstruct
    protected void init() {
        // JWT Secret 키 검증 및 초기화
        validateSecretKey();
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private void validateSecretKey() {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
    }

    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    // 로그인 시점 (User 엔티티)
    private String generateToken(User user, Long expirationTime) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim(USER_ID, user.getId())
                .claim(ROLE, user.getRole().name())
                .signWith(signingKey)
                .compact();
    }

    // 이미 인증된 상태에서 토큰 재발급(Authentication)
    public String generateTokenFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof JwtUserPrincipal) {
            JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
            return generateTokenFromPrincipal(principal, ACCESS_TOKEN_EXPIRATION_TIME);
        } else if (authentication.getPrincipal() instanceof Long) {
            // 현재 구조 호환성을 위해 추가
            Long userId = (Long) authentication.getPrincipal();
            // 실제로는 DB에서 사용자 정보를 조회해야 함
            throw new IllegalArgumentException("Cannot generate token from Long principal. Use User entity instead.");
        }
        throw new IllegalArgumentException("Unsupported principal type");
    }

    private String generateTokenFromPrincipal(JwtUserPrincipal principal, Long expirationTime) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(principal.userId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim(USER_ID, principal.userId())
                .claim(ROLE, principal.role().name())
                .signWith(signingKey)
                .compact();
    }

    // 토큰으로부터 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = getBody(token);

        Long userId = Long.valueOf(claims.get(USER_ID).toString());
        String roleStr = claims.get(ROLE, String.class);

        Role role = Role.valueOf(roleStr);
        Collection<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(role.name()));

        JwtUserPrincipal principal = new JwtUserPrincipal(userId, null, role, authorities);
        return new JwtAuthenticationToken(principal, token, authorities);
    }

    // 토큰 검증
    public JwtValidationType validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return JwtValidationType.EMPTY_JWT;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return JwtValidationType.VALID_JWT;
        } catch (ExpiredJwtException ex) {
            log.warn("JWT token expired");
            return JwtValidationType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token");
            return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token");
            return JwtValidationType.INVALID_JWT_TOKEN;
        } catch (IllegalArgumentException ex) {
            log.warn("JWT token compact of handler are invalid");
            return JwtValidationType.EMPTY_JWT;
        } catch (Exception ex) {
            log.error("JWT token validation failed", ex);
            return JwtValidationType.INVALID_JWT_TOKEN;
        }
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserFromJwt(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("JWT token is null or empty");
        }

        try {
            Claims claims = getBody(token);
            Object userIdObj = claims.get(USER_ID);
            if (userIdObj == null) {
                throw new IllegalArgumentException("JWT token does not contain userId");
            }
            return Long.valueOf(userIdObj.toString());
        } catch (JwtException ex) {
            throw new IllegalArgumentException("Invalid JWT token", ex);
        }
    }

    private Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}