package com.example.stockmate.member.Exception;


import com.example.stockmate.global.exception.GeneralException;
import com.example.stockmate.global.response.code.status.ErrorStatus;

public class EmailAlreadyExistException extends GeneralException {
    public EmailAlreadyExistException() {
        super(ErrorStatus.EMAIL_ALREADY_EXISTS);
    }
}