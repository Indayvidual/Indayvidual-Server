package com.indayvidual.server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class UpdateUsername {
        @Schema(description = "새 닉네임", example = "박감자", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "username은 필수입니다.")
        @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
        private String username;
    }

    @Getter
    public static class UpdatePassword {
        @Schema(description = "현재 비밀번호", example = "oldP@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "currentPassword는 필수입니다.")
        private String currentPassword;
        @Schema(description = "새 비밀번호(최소 8자)", example = "NewP@ssw0rd1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "newPassword는 필수입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String newPassword;
    }

    // 이미지 업로드는 Multipart 사용 (컨트롤러에서 @RequestPart 로 받음)
    // 필요 시 URL만 받는 방식(프리사인드 업로드) DTO도 추가 가능
}
