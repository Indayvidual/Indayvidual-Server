package com.indayvidual.server.domain.memo.exception;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.exception.GeneralException;

public class MemoException extends GeneralException {
	public MemoException(BaseErrorCode code) {
		super(code);
	}
}
