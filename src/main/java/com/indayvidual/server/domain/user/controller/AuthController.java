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
import com.indayvidual.server.global.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import jakarta.validation.Valid;
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

    @Operation(
            summary = "회원가입",
            description = "이메일과 비밀번호, 이름, 전화번호를 입력하여 회원가입을 진행합니다. 이미 가입된 이메일인 경우 실패합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이메일/닉네임 중복")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDTO>> signup(@RequestBody @Valid SignupRequestDTO request) {
        SignupResponseDTO response = userAuthService.signupWithEmail(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 사용하여 로그인합니다. 성공 시 Access Token과 Refresh Token을 발급합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = userAuthService.loginWithEmailAndPassword(
                request.getEmail(), request.getPassword()
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 OAuth 액세스 토큰을 이용해 로그인합니다. 기존 회원이면 로그인, 아니면 자동 회원가입 후 로그인 처리됩니다."
    )
    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> kakaoLogin(
            @RequestBody @Valid KakaoLoginRequestDTO req) {

        LoginResponseDTO dto = kakaoAuthService.loginWithKakao(req.getAccessToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(dto));
    }

    @Operation(
            summary = "Access Token 재발급",
            description = "Refresh Token을 이용해 새로운 Access Token과 Refresh Token을 발급합니다. 만료되거나 유효하지 않은 Refresh Token은 거부됩니다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(
            @RequestHeader(value = "Refresh-Token", required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new GeneralException(ErrorStatus.AUTH_REFRESH_MISSING);
            }
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        LoginResponseDTO dto = refreshTokenService.rotate(refreshToken);
        return ResponseEntity.ok(ApiResponse.onSuccess(dto));
    }

    @Operation(
            summary = "로그아웃",
            description = "Refresh Token을 사용하여 로그아웃 처리합니다. 해당 토큰은 폐기되며 재사용할 수 없습니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Refresh-Token", required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new GeneralException(ErrorStatus.AUTH_REFRESH_MISSING);
        }
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        userAuthService.logoutWithRefreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃 완료"));
    }

}
