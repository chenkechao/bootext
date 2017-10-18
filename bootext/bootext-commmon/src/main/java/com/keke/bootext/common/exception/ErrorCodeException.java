package com.keke.bootext.common.exception;

import com.keke.bootext.common.enums.ErrorCode;

import java.text.MessageFormat;

public class ErrorCodeException extends BaseException {
    public ErrorCodeException(ErrorCode code, Object... args) {
        super(code.getCode(), MessageFormat.format(code.getMessage(),args));
    }


    public ErrorCodeException(Throwable e, ErrorCode code, Object... args) {
        super(code.getCode(), MessageFormat.format(code.getMessage(),args),e);
    }
}
