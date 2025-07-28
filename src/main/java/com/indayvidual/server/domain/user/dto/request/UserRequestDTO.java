package com.indayvidual.server.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class UpdateUsername {
        @NotBlank
        @Size(min = 2, max = 20)
        private String username;
    }

    @Getter
    public static class UpdatePassword {
        @NotBlank
        private String currentPassword;
        @NotBlank
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String newPassword;
    }

    // 이미지 업로드는 Multipart 사용 (컨트롤러에서 @RequestPart 로 받음)
    // 필요 시 URL만 받는 방식(프리사인드 업로드) DTO도 추가 가능
}
