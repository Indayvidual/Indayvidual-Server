package com.indayvidual.server.domain.user.dto.request;

import lombok.Getter;

@Getter
public class DeleteAccountRequest {
    private boolean hard;     // true면 하드 삭제
}
