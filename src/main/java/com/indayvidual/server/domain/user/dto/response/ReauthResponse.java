package com.indayvidual.server.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReauthResponse {
    private String reauthToken; // UUID
    private long expiresInSeconds; // 예: 300
}
