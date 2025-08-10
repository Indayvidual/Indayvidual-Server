package com.indayvidual.server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @Schema(description = "로그인 이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(description = "로그인 비밀번호", example = "P@ssw0rd123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
