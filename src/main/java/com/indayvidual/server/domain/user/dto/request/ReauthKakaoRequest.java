package com.indayvidual.server.domain.user.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReauthKakaoRequest {
    @Schema(description = "카카오 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.KAKAO_ACCESS", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "kakaoAccessToken은 필수입니다.")
    private String kakaoAccessToken;
}
