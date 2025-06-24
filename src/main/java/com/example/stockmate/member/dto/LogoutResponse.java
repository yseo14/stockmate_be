package com.example.stockmate.member.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutResponse {
    private LocalDateTime logoutAt;
}
