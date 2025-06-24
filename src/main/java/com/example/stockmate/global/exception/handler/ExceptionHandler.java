package com.example.stockmate.global.exception.handler;


import com.example.stockmate.global.exception.GeneralException;
import com.example.stockmate.global.response.code.BaseErrorCode;

public class ExceptionHandler extends GeneralException {
    public ExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}