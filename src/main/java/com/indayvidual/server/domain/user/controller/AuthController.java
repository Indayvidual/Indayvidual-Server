package com.indayvidual.server.domain.user.controller;

import com.indayvidual.server.domain.user.dto.request.KakaoLoginRequestDTO;
import com.indayvidual.server.domain.user.dto.request.LoginRequestDTO;
import com.indayvidual.server.domain.user.dto.response.LoginResponseDTO;
import com.indayvidual.server.domain.user.dto.request.SignupRequestDTO;
import com.indayvidual.server.domain.user.dto.response.SignupResponseDTO;
import com.indayvidual.server.domain.user.service.AuthService.KakaoAuthService;
import com.indayvidual.server.domain.user.service.UserService.RefreshTokenService;
import com.indayvidual.server.domain.user.service.UserService.UserAuthService;
import com.indayvidual.server.global.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "회원가입, 로그인 기능 제공")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAuthService userAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDTO>> signup(@RequestBody SignupRequestDTO request) {
        SignupResponseDTO response = userAuthService.signupWithEmail(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userAuthService.loginWithEmailAndPassword(
                request.getEmail(), request.getPassword()
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> kakaoLogin(
            @RequestBody KakaoLoginRequestDTO req) {

        LoginResponseDTO dto = kakaoAuthService.loginWithKakao(req.getAccessToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(
            @RequestHeader("Refresh-Token") String refreshToken
    ) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        LoginResponseDTO dto = refreshTokenService.rotate(refreshToken);
        return ResponseEntity.ok(ApiResponse.onSuccess(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Authorization") String bearerToken
    ) {
        String refreshToken = bearerToken.replace("Bearer ", "");
        userAuthService.logoutWithRefreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃 완료"));
    }

}
