package com.indayvidual.server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EmailVerifyRequestDTO {
    @Schema(description = "인증 대상 이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Schema(description = "수신한 인증 코드(4자리 숫자)", example = "1234", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^[0-9]{4}$", message = "인증번호는 4자리 숫자여야 합니다.")
    @NotBlank(message = "인증번호는 필수입니다.")
    private String code;
}