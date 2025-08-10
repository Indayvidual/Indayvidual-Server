package com.indayvidual.server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReauthPasswordRequest {
    @Schema(description = "현재 비밀번호", example = "P@ssw0rd123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "currentPassword는 필수입니다.")
    private String currentPassword;
}