package com.example.stockmate.member.Exception;

import com.example.stockmate.global.exception.GeneralException;
import com.example.stockmate.global.response.code.status.ErrorStatus;

public class MemberNotFoundException extends GeneralException {
    public MemberNotFoundException() {
        super(ErrorStatus.MEMBER_NOT_FOUND);
    }
}
