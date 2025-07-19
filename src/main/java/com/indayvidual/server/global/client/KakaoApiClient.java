package com.indayvidual.server.global.client;

import com.indayvidual.server.domain.user.dto.external.KakaoProfile;
import com.indayvidual.server.domain.user.dto.external.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiClient {

    @Value("${kakao.oauth.token-info-uri}")
    private String tokenInfoUri;

    @Value("${kakao.oauth.user-info-uri}")
    private String userInfoUri;

    private final WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    /** 토큰 검증 + 프로필 반환 */
    public KakaoProfile fetchProfile(String accessToken) {

        // 1) 토큰 유효성 검사
        TokenInfo tokenInfo = webClient.get()
                .uri(tokenInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(TokenInfo.class)
                .block();

        // (옵션) app_id 일치 여부, 만료까지 남은 시간 확인 가능
        log.debug("kakao token expires_in={}", tokenInfo.getExpiresIn());

        // 2) 프로필 조회
        KakaoProfile profile = webClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoProfile.class)
                .block();

        return profile;
    }
}

