package com.example.stockmate.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String name;
    private String accessToken;
    private String refreshToken;
}