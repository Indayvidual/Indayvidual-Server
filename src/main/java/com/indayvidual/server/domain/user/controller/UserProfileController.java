package com.indayvidual.server.domain.user.controller;

import com.indayvidual.server.domain.user.dto.request.DeleteAccountRequest;
import com.indayvidual.server.domain.user.dto.request.UserRequestDTO;
import com.indayvidual.server.domain.user.dto.response.SimpleMessageResponse;
import com.indayvidual.server.domain.user.dto.response.UserResponseDTO;
import com.indayvidual.server.domain.user.service.AuthService.ReauthService;
import com.indayvidual.server.domain.user.service.UserService.UserProfileService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.config.security.JwtUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "MyPage API", description = "마이페이지 기능 제공")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final ReauthService reauthService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDTO.Profile>> getMyProfile(
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        reauthService.assertReauthOrThrow(principal.userId(), reauthToken, false);
        var profile = userProfileService.getMyProfile(principal.userId());
        return ResponseEntity.ok(ApiResponse.onSuccess(profile));
    }

    @PatchMapping("/update_username")
    public ResponseEntity<ApiResponse<String>> updateUsername(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @RequestBody @Valid UserRequestDTO.UpdateUsername dto
    ) {
        reauthService.assertReauthOrThrow(principal.userId(), reauthToken, false);
        userProfileService.updateUsername(principal.userId(), dto.getUsername());
        return ResponseEntity.ok(ApiResponse.onSuccess("닉네임이 변경되었습니다."));
    }

    @PatchMapping("/update_password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @RequestBody @Valid UserRequestDTO.UpdatePassword dto
    ) {
        reauthService.assertReauthOrThrow(principal.userId(), reauthToken, true); // 1회 소비
        userProfileService.updatePassword(principal.userId(), dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok(ApiResponse.onSuccess("비밀번호가 변경되었습니다."));
    }

    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProfileImage(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestHeader("X-Reauth-Token") String reauthToken,
            @RequestPart("image") MultipartFile image
    ) {
        reauthService.assertReauthOrThrow(principal.userId(), reauthToken, false);
        String url = userProfileService.updateProfileImage(principal.userId(), image);
        return ResponseEntity.ok(ApiResponse.onSuccess(url));
    }

    @Operation(summary = "회원 탈퇴", description = "재인증 토큰(X-Reauth-Token) 필요. 기본은 소프트 삭제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<SimpleMessageResponse>> deleteMe(
            @AuthenticationPrincipal JwtUserPrincipal principal,
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
