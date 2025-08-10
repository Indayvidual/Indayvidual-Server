package com.indayvidual.server.global.client;

import com.indayvidual.server.domain.user.dto.external.KakaoProfile;
import com.indayvidual.server.domain.user.dto.external.TokenInfo;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiClient {

    @Value("${kakao.oauth.token-info-uri}")
    private String tokenInfoUri;

    @Value("${kakao.oauth.user-info-uri}")
    private String userInfoUri;

    // 주입형 WebClient (타임아웃/풀 설정은 별도 Config에서)
    private final WebClient kakaoWebClient;

    public KakaoProfile fetchProfile(String accessToken) {
        // 1) 토큰 유효성 검사 (선택사항 - 완전히 제거해도 됨)
        TokenInfo tokenInfo = kakaoWebClient.get()
                .uri(tokenInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        Mono.error(new GeneralException(ErrorStatus.AUTH_OAUTH_INVALID_TOKEN)))
                .onStatus(HttpStatusCode::is5xxServerError, resp ->
                        Mono.error(new GeneralException(ErrorStatus.AUTH_OAUTH_PROVIDER_ERROR)))
                .bodyToMono(TokenInfo.class)
                .block();

        // 만료 시간만 확인 (선택사항)
        if (tokenInfo != null && tokenInfo.getExpiresIn() != null) {
            log.debug("kakao token expires_in={}", tokenInfo.getExpiresIn());
        }

        // 2) 프로필 조회 (핵심)
        KakaoProfile profile = kakaoWebClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        Mono.error(new GeneralException(ErrorStatus.AUTH_OAUTH_INVALID_TOKEN)))
                .onStatus(HttpStatusCode::is5xxServerError, resp ->
                        Mono.error(new GeneralException(ErrorStatus.AUTH_OAUTH_PROVIDER_ERROR)))
                .bodyToMono(KakaoProfile.class)
                .block();

        if (profile == null || profile.getId() == null) {
            throw new GeneralException(ErrorStatus.AUTH_OAUTH_PROVIDER_ERROR);
        }
        return profile;
    }
}

