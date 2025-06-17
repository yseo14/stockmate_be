package com.example.stockmate.member.application.mapper;

import com.example.stockmate.global.enums.Role;
import com.example.stockmate.member.domain.Member;
import com.example.stockmate.member.dto.SignUpRequest;
import com.example.stockmate.member.dto.SignUpResponse;

public class SignUpMapper {

    public static Member toMember(SignUpRequest dto, String encodedPassword, String providerId) {
        return Member.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .name(dto.getName())
                .provider("local")
                .providerId(providerId)
                .role(Role.USER)
                .build();
    }

    public static SignUpResponse toSignUpResponse(Member member) {
        return SignUpResponse.builder()
                .name(member.getName())
                .build();
    }
}
