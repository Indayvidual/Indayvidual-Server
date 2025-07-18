package com.indayvidual.server.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EmailVerifyRequestDTO {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Pattern(regexp = "^[0-9]{4}$", message = "인증번호는 4자리 숫자여야 합니다.")
    @NotBlank(message = "인증번호는 필수입니다.")
    private String code;
}