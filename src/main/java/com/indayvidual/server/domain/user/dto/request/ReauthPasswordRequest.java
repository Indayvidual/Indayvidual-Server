package com.indayvidual.server.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReauthPasswordRequest {
    @NotBlank
    private String currentPassword;
}