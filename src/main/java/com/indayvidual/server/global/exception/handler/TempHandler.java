package com.indayvidual.server.global.exception.handler;

import com.indayvidual.server.global.exception.GeneralException;
import com.indayvidual.server.global.api.code.BaseErrorCode;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}