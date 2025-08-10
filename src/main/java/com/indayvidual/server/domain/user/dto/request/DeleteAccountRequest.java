package com.indayvidual.server.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeleteAccountRequest {
    @Schema(description = "true면 하드 삭제, false면 소프트 삭제", example = "false")
    private boolean hard;     // true면 하드 삭제
}
