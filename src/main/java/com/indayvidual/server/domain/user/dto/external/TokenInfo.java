package com.indayvidual.server.domain.user.dto.external;

import lombok.Getter;

@Getter
public class TokenInfo {
    private Long id;
    private Long expiresIn;
    private Long appId;
}
