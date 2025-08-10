package com.indayvidual.server.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReauthResponse {
    @Schema(description = "재인증 토큰(Base64URL)", example = "dGhpcy1pcy1yZWF1dGgtdG9rZW4", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reauthToken;
    @Schema(description = "만료까지 남은 초", example = "600", requiredMode = Schema.RequiredMode.REQUIRED)
    private long expiresInSeconds;
}
