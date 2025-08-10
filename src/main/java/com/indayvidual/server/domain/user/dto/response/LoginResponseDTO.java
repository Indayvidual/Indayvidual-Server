package com.indayvidual.server.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.TOKEN_SAMPLE")
    private String accessToken;
    @Schema(description = "JWT 리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0In0.REFRESH_SAMPLE")
    private String refreshToken;
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;
    @Schema(description = "사용자 이름", example = "박성준")
    private String username;
    @Schema(description = "역할(권한)", example = "ROLE_USER")
    private String role;
}