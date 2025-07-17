package com.indayvidual.server.domain.habit.exception;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.exception.GeneralException;

public class HabitException extends GeneralException {
	public HabitException(BaseErrorCode code) {
		super(code);
	}
}
