package com.indayvidual.server.global.api.code;

import com.indayvidual.server.global.api.response.ErrorReasonDTO;

public interface BaseErrorCode {

    ErrorReasonDTO getReason();

    ErrorReasonDTO getReasonHttpStatus();
}
