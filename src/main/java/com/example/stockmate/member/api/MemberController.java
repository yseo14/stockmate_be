package com.example.stockmate.member.api;

import com.example.stockmate.global.response.ApiResponse;
import com.example.stockmate.global.response.code.status.SuccessStatus;
import com.example.stockmate.member.application.service.MemberService;
import com.example.stockmate.member.dto.LoginRequest;
import com.example.stockmate.member.dto.LoginResponse;
import com.example.stockmate.member.dto.SignUpRequest;
import com.example.stockmate.member.dto.SignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ApiResponse<SignUpResponse> signup(@RequestBody @Valid SignUpRequest request) {
        SignUpResponse signUpResponseDto = memberService.signUp(request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_SIGNUP_SUCCESS, signUpResponseDto);
    }

    @GetMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = memberService.login(request);
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_LOGIN_SUCCESS, loginResponse);
    }
}
