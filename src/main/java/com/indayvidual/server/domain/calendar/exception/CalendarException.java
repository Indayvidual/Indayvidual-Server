package com.indayvidual.server.domain.calendar.exception;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.exception.GeneralException;

public class CalendarException extends GeneralException {
    public CalendarException(BaseErrorCode code) {
        super(code);
    }
}
