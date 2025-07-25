package com.indayvidual.server.domain.timetable.exception;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.exception.GeneralException;

public class TimetableException extends GeneralException {
    public TimetableException(BaseErrorCode code) { super(code); }
}