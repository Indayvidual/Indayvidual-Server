package com.indayvidual.server.domain.user.service.AuthService;

import com.indayvidual.server.domain.user.dto.response.ReauthResponse;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.UserProvider;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import com.indayvidual.server.domain.user.repository.UserProviderRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.client.KakaoApiClient;
import com.indayvidual.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReauthService {

    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redis; // Lettuce/Jedis 아무거나
    private final KakaoApiClient kakaoApiClient;

    private static final Duration REAUTH_TTL = Duration.ofMinutes(10);
    private static final SecureRandom RNG = new SecureRandom();

    public ReauthResponse reauthByPassword(Long userId, String currentPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REAUTH_USER_NOT_FOUND));

        if (user.getPassword() == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new GeneralException(ErrorStatus.REAUTH_PASSWORD_MISMATCH);
        }
        return issueToken(userId, "PASSWORD");
    }

    public ReauthResponse reauthByKakao(Long userId, String kakaoAccessToken) {
        // 1) kakaoAccessToken 으로 카카오 유저 정보 조회 (userinfo)
        var profile = kakaoApiClient.fetchProfile(kakaoAccessToken); // 아래 참고

        // 2) 우리 시스템에 연동된 카카오 계정인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REAUTH_USER_NOT_FOUND));
        boolean linked = userProviderRepository
                .findByUserAndProvider(user, Provider.KAKAO)
                .filter(UserProvider::getIsActive)
                .map(up -> Objects.equals(up.getProviderId(), String.valueOf(profile.getId())))
                .orElse(false);

        if (!linked) throw new GeneralException(ErrorStatus.REAUTH_PROVIDER_NOT_LINKED);

        return issueToken(userId, "KAKAO");
    }

    private ReauthResponse issueToken(Long userId, String method) {
        //String token = UUID.randomUUID().toString();
        // 더 예측 어려운 토큰(Base64URL 32바이트)
        byte[] buf = new byte[32];
        RNG.nextBytes(buf);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(buf);

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
        if (v == null) throw new GeneralException(ErrorStatus.REAUTH_REQUIRED);

        if (consumeOnce) {
            redis.delete(key);
        }
    }

    private String key(Long userId, String token) {
        return "reauth:" + userId + ":" + token;
    }

//    // --- Kakao userinfo 호출 ---
//    private KakaoUserInfo fetchKakaoUserInfo(String kakaoAccessToken) {
//        WebClient web = WebClient.builder()
//                .baseUrl(kakaoApiBase)
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
//                .build();
//
//        return web.get()
//                .uri("/v2/user/me")
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, rsp ->
//                        rsp.bodyToMono(String.class)
//                                .map(body -> new IllegalArgumentException("Kakao 인증 실패: " + body)))
//                .bodyToMono(KakaoUserInfo.class)
//                .block();
//    }
}
