package com.indayvidual.server.domain.user.controller;

import com.indayvidual.server.domain.user.dto.request.ReauthKakaoRequest;
import com.indayvidual.server.domain.user.dto.request.ReauthPasswordRequest;
import com.indayvidual.server.domain.user.dto.response.ReauthResponse;
import com.indayvidual.server.domain.user.service.AuthService.ReauthService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.config.security.JwtUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Re-Auth(재인증)", description = "민감 정보 변경 전 재인증(비밀번호/카카오)")
@RestController
@RequestMapping("/api/auth/re-auth")
@RequiredArgsConstructor
public class ReauthController {
    private final ReauthService reauthService;

    @Operation(summary = "비밀번호 재인증", description = "현재 비밀번호 검증 후 reauth_token 발급(TTL=5분)")
    @PostMapping("/password")
    public ResponseEntity<ApiResponse<ReauthResponse>> reauthByPassword(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody @Valid ReauthPasswordRequest req) {
        var res = reauthService.reauthByPassword(principal.userId(), req.getCurrentPassword());
        return ResponseEntity.ok(ApiResponse.onSuccess(res));
    }

    @Operation(summary = "카카오 재인증", description = "카카오 accessToken 검증 후 reauth_token 발급(TTL=5분)")
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<ReauthResponse>> reauthByKakao(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody @Valid ReauthKakaoRequest req) {
        var res = reauthService.reauthByKakao(principal.userId(), req.getKakaoAccessToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(res));
    }
}
