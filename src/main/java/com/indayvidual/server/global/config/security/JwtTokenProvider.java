package com.indayvidual.server.global.config.security;

import com.indayvidual.server.domain.user.entity.*;
import com.indayvidual.server.domain.user.entity.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.lang.Nullable;
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
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String USER_ID = "userId";
    private static final String ROLE = "role";
    private static final String TOKEN_TYPE = "tokenType";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000L;  // 15분
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 14 * 24 * 60 * 60 * 1000L; // 14일

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
        return generateToken(user, ACCESS_TOKEN_EXPIRATION_TIME, TOKEN_TYPE_ACCESS, null);
    }

    public String generateRefreshToken(User user) {
        String tokenId = UUID.randomUUID().toString();   // ★ jti
        return generateToken(user, REFRESH_TOKEN_EXPIRATION_TIME, TOKEN_TYPE_REFRESH, tokenId);
    }

    // 공통 로직
    private String generateToken(User user,
                                 long expirationTime,
                                 String tokenType,
                                 @Nullable String jti) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        JwtBuilder builder = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim(USER_ID, user.getId())
                .claim(ROLE, user.getRole().name())
                .claim(TOKEN_TYPE, tokenType)     // ★ access / refresh
                .signWith(signingKey);

        if (jti != null) {
            builder.setId(jti);
            log.info("jti 추가됨: {}", jti);
        }        // ★ refresh 전용

        return builder.compact();
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
        if (!StringUtils.hasText(token)) return JwtValidationType.EMPTY_JWT;

        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
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
        } catch (Exception ex) {
            log.error("JWT validation failed", ex);
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

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료되었어도 Claims는 추출 가능
        } catch (JwtException | IllegalArgumentException e) {
            // 구조가 잘못됐거나 서명 오류, null 등
            log.warn("JWT 파싱 실패: {}", e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }

    public boolean isRefreshToken(String token) {
        return TOKEN_TYPE_REFRESH.equals(getBody(token).get(TOKEN_TYPE, String.class));
    }

    private Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}