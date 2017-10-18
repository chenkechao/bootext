package com.keke.bootext.common.enums;

public enum ExceptionEnum implements ErrorCode {

    HTTP_UNKNOWN_ERROR("001","http错误");

    private String code;
    private String message;

    ExceptionEnum(String code, String message) {
        this.code=code;
        this.message=message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
