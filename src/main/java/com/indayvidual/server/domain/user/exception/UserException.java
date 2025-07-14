package com.indayvidual.server.domain.user.exception;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.exception.GeneralException;

public class UserException extends GeneralException {
	public UserException(BaseErrorCode code) {
		super(code);
	}
}
