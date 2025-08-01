package com.indayvidual.server.domain.user.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReauthKakaoRequest {
    @NotBlank
    private String kakaoAccessToken;
}
