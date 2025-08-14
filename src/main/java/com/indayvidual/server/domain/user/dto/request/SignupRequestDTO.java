package com.indayvidual.server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {

    @Schema(description = "회원가입 이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(description = "회원가입 비밀번호(8자 이상)", example = "P@ssw0rd123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @Schema(description = "사용자 이름", example = "박성준", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
    @Pattern(regexp = com.indayvidual.server.common.Patterns.USERNAME_JS_COMPAT,
                         message = "닉네임은 한글/영문/숫자/공백/[_-]만 허용합니다.")
    private String username;

    @Schema(description = "전화번호(선택)", example = "010-1234-5678")
    @Pattern(regexp = "^01[0-9]-?\\d{3,4}-?\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;
}
