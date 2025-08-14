package com.indayvidual.server.domain.user.controller;

import com.indayvidual.server.common.Patterns;
import com.indayvidual.server.domain.user.dto.request.DeleteAccountRequest;
import com.indayvidual.server.domain.user.dto.request.UserRequestDTO;
import com.indayvidual.server.domain.user.dto.response.SimpleMessageResponse;
import com.indayvidual.server.domain.user.dto.response.UserResponseDTO;
import com.indayvidual.server.domain.user.service.AuthService.ReauthService;
import com.indayvidual.server.domain.user.service.UserService.UserProfileService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.config.security.JwtUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "MyPage API", description = "마이페이지 기능 제공")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final ReauthService reauthService;

    @Operation(
            summary = "내 프로필 조회",
            description = "액세스 토큰만으로 내 프로필을 반환합니다. 재인증 토큰은 필요하지 않습니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패(액세스 토큰 누락/무효)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO.Profile>> getMyProfile(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        var profile = userProfileService.getMyProfile(principal.userId());
        return ResponseEntity.ok(ApiResponse.onSuccess(profile));
    }

    @Operation(
            summary = "닉네임 중복 확인",
            description = "입력한 닉네임이 사용 가능한지 확인합니다. (본인 닉네임은 중복으로 간주하지 않음)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용 가능 여부 반환"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "형식 오류"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/username/check")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Parameter(description = "확인할 닉네임", example = "감자도리")
            @RequestParam @NotBlank(message = "username은 필수입니다.")
            @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
            @Pattern(regexp = Patterns.USERNAME_JS_COMPAT, message = "닉네임은 한글/영문/숫자/공백/[_-]만 허용합니다.")
            String username
    ) {
                boolean available = userProfileService.isUsernameAvailable(username, principal.userId());
                return ResponseEntity.ok(ApiResponse.onSuccess(available));
    }

    @Operation(summary = "닉네임 변경", description = "재인증 토큰 검증 후 닉네임을 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "재인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @PatchMapping("/update_username")
    public ResponseEntity<ApiResponse<String>> updateUsername(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Parameter(name = "X-Reauth-Token", in = ParameterIn.HEADER, required = true,
                    description = "재인증 토큰", example = "reauth_base64url_token")
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @RequestBody @Valid UserRequestDTO.UpdateUsername dto
    ) {
        reauthService.assertReauthOrThrow(principal.userId(), reauthToken, false);
        userProfileService.updateUsername(principal.userId(), dto.getUsername());
        return ResponseEntity.ok(ApiResponse.onSuccess("닉네임이 변경되었습니다."));
    }

    @Operation(summary = "비밀번호 변경", description = "재인증 토큰 1회용 검증 후 비밀번호를 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "새 비밀번호가 현재와 동일"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "재인증 필요 또는 현재 비밀번호 불일치"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "비밀번호 기반 변경이 불가능한 계정(소셜-only/LOCAL 미연동)")
    })
    @PatchMapping("/update_password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Parameter(name = "X-Reauth-Token", in = ParameterIn.HEADER, required = true,
                    description = "재인증 토큰", example = "reauth_base64url_token")
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @RequestBody @Valid UserRequestDTO.UpdatePassword dto
    ) {
        reauthService.assertReauthOrThrow(principal.userId(), reauthToken, true); // 1회 소비
        userProfileService.updatePassword(principal.userId(), dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok(ApiResponse.onSuccess("비밀번호가 변경되었습니다."));
    }

    @Operation(summary = "프로필 이미지 변경",
                           description = "재인증 토큰 검증 후 이미지를 업로드합니다. 허용 형식: JPEG/PNG/WEBP, 최대 5MB.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "변경 성공 (프리사인드 URL 반환)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 파일 혹은 허용되지 않은 형식"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "재인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "413", description = "파일 용량 초과"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "업로드 실패")
    })
    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProfileImage(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Parameter(name = "X-Reauth-Token", in = ParameterIn.HEADER, required = true,
                    description = "재인증 토큰", example = "reauth_base64url_token")
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @RequestPart("image") MultipartFile image
    ) {
        reauthService.assertReauthOrThrow(principal.userId(), reauthToken, false);
        String url = userProfileService.updateProfileImage(principal.userId(), image);
        return ResponseEntity.ok(ApiResponse.onSuccess(url));
    }

    @Operation(summary = "회원 탈퇴", description = "재인증 토큰(X-Reauth-Token) 필요. 기본은 소프트 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "재인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<SimpleMessageResponse>> deleteMe(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @Parameter(name = "X-Reauth-Token", in = ParameterIn.HEADER, required = true,
                    description = "재인증 토큰", example = "reauth_base64url_token")
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @RequestBody(required = false) DeleteAccountRequest req
    ) {
        if (req == null) req = new DeleteAccountRequest(); // 기본값
        userProfileService.deleteMyAccount(principal.userId(), reauthToken, req);
        return ResponseEntity.ok(ApiResponse.onSuccess(
                SimpleMessageResponse.builder().message("탈퇴가 완료되었습니다.").build()
        ));
    }
}
