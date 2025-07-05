package com.indayvidual.server.global.api.code;

import com.indayvidual.server.global.api.response.ReasonDTO;

public interface BaseCode {

    ReasonDTO getReason();

    ReasonDTO getReasonHttpStatus();
}