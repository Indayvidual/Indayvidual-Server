package com.indayvidual.server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoLoginRequestDTO {

    @Schema(description = "카카오 OAuth 액세스 토큰", example = "AAAAQwAAABBh...kaKaoAccessTokenSample", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken은 필수입니다.")
    private String accessToken;
}

