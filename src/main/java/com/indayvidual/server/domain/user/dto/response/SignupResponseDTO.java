package com.indayvidual.server.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponseDTO {
    @Schema(description = "생성된 사용자 ID", example = "1")
    private Long userId;
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;
    @Schema(description = "사용자 이름", example = "박성준")
    private String username;
    @Schema(description = "결과 메시지", example = "회원가입이 완료되었습니다.")
    private String message;
}
