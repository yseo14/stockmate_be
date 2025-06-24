package com.example.stockmate.member.application.mapper;

import com.example.stockmate.global.jwt.JwtToken;
import com.example.stockmate.member.domain.Member;
import com.example.stockmate.member.dto.LoginResponse;

public class LoginMapper {
    public static LoginResponse toLoginResponse(Member member, JwtToken jwtToken) {
        return LoginResponse.builder()
                .name(member.getName())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
