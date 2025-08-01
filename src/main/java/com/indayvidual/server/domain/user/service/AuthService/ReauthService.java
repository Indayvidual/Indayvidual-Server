package com.indayvidual.server.domain.user.service.AuthService;

import com.indayvidual.server.domain.user.dto.external.KakaoUserInfo;
import com.indayvidual.server.domain.user.dto.response.ReauthResponse;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.UserProvider;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import com.indayvidual.server.domain.user.repository.UserProviderRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;// ReauthService.java 내
import org.springframework.http.HttpStatusCode;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReauthService {

    @Value("${kakao.api-base}")
    private String kakaoApiBase;

    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redis; // Lettuce/Jedis 아무거나

    private static final Duration REAUTH_TTL = Duration.ofMinutes(5);

    public ReauthResponse reauthByPassword(Long userId, String currentPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getPassword() == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return issueToken(userId, "PASSWORD");
    }

    public ReauthResponse reauthByKakao(Long userId, String kakaoAccessToken) {
        // 1) kakaoAccessToken 으로 카카오 유저 정보 조회 (userinfo)
        KakaoUserInfo info = fetchKakaoUserInfo(kakaoAccessToken); // 아래 참고

        // 2) 우리 시스템에 연동된 카카오 계정인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        boolean linked = userProviderRepository
                .findByUserAndProvider(user, Provider.KAKAO)
                .filter(UserProvider::getIsActive)
                .map(up -> Objects.equals(up.getProviderId(), String.valueOf(info.getId())))
                .orElse(false);

        if (!linked) throw new IllegalArgumentException("연결된 카카오 계정이 아닙니다.");

        return issueToken(userId, "KAKAO");
    }

    private ReauthResponse issueToken(Long userId, String method) {
        String token = UUID.randomUUID().toString();
        String key = key(userId, token);
        String value = "{\"method\":\"" + method + "\",\"ts\":\"" + Instant.now() + "\"}";

        redis.opsForValue().set(key, value, REAUTH_TTL);
        return ReauthResponse.builder()
                .reauthToken(token)
                .expiresInSeconds(REAUTH_TTL.toSeconds())
                .build();
    }

    public void assertReauthOrThrow(Long userId, String reauthToken, boolean consumeOnce) {
        String key = key(userId, reauthToken);
        String v = redis.opsForValue().get(key);
        if (v == null) throw new IllegalArgumentException("재인증이 필요합니다.");

        if (consumeOnce) {
            redis.delete(key);
        }
    }

    private String key(Long userId, String token) {
        return "reauth:" + userId + ":" + token;
    }

    // --- Kakao userinfo 호출 ---
    private KakaoUserInfo fetchKakaoUserInfo(String kakaoAccessToken) {
        WebClient web = WebClient.builder()
                .baseUrl(kakaoApiBase)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                .build();

        return web.get()
                .uri("/v2/user/me")
                .retrieve()
                .onStatus(HttpStatusCode::isError, rsp ->
                        rsp.bodyToMono(String.class)
                                .map(body -> new IllegalArgumentException("Kakao 인증 실패: " + body)))
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }
}
