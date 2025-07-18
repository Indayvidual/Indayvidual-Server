package com.indayvidual.server.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponseDTO {
    private Long userId;
    private String email;
    private String username;
    private String message;
}
