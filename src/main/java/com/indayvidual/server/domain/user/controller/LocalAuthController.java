package com.indayvidual.server.domain.user.controller;

import com.indayvidual.server.domain.user.dto.request.LoginRequestDTO;
import com.indayvidual.server.domain.user.dto.response.LoginResponseDTO;
import com.indayvidual.server.domain.user.dto.request.SignupRequestDTO;
import com.indayvidual.server.domain.user.service.UserService.UserAuthService;
import com.indayvidual.server.global.api.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LocalAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> signup(@RequestBody SignupRequestDTO request) {
        LoginResponseDTO response = userAuthService.signupWithEmail(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userAuthService.loginWithEmailAndPassword(
                request.getEmail(), request.getPassword()
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        // 서버에서는 토큰 삭제 대신, 이후 요청 차단을 위한 로그 또는 blacklist 처리 가능
        String token = extractToken(request);
        log.info("사용자 로그아웃 요청: {}", token);

        // TODO: Redis에 token blacklist 등록, DB에 refresh token 제거 등 추가 가능
        return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃 완료"));
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
