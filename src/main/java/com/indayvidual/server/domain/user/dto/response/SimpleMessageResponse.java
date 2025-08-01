package com.indayvidual.server.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleMessageResponse {
    private String message;
}

