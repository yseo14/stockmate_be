package com.example.stockmate.member.Exception;

import com.example.stockmate.global.exception.GeneralException;
import com.example.stockmate.global.response.code.status.ErrorStatus;

public class PasswordNotMatchedException extends GeneralException {
    public PasswordNotMatchedException() {
        super(ErrorStatus.PASSWORD_NOT_MATCHED);
    }
}
