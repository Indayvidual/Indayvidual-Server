package com.indayvidual.server.domain.event.exception;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.exception.GeneralException;

public class EventException extends GeneralException {
    public EventException(BaseErrorCode code) {
        super(code);
    }
}