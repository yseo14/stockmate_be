package com.example.stockmate.member.application.mapper;

import com.example.stockmate.member.dto.LogoutResponse;
import java.time.LocalDateTime;

public class LogoutMapper {
    public static LogoutResponse toLogoutResponse() {
        return LogoutResponse.builder()
                .logoutAt(LocalDateTime.now())
                .build();
    }
}
