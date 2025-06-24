package com.example.stockmate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.stockmate.global.jwt.JwtToken;
import com.example.stockmate.global.jwt.JwtUtils;
import com.example.stockmate.member.Exception.EmailAlreadyExistException;
import com.example.stockmate.member.Exception.MemberNotFoundException;
import com.example.stockmate.member.Exception.PasswordNotMatchedException;
import com.example.stockmate.member.application.service.MemberService;
import com.example.stockmate.member.domain.Member;
import com.example.stockmate.member.dto.LoginRequest;
import com.example.stockmate.member.dto.LoginResponse;
import com.example.stockmate.member.dto.SignUpRequest;
import com.example.stockmate.member.dto.SignUpResponse;
import com.example.stockmate.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Test
    void 회원가입_성공() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@example.com")
                .password("password123")
                .name("테스트유저")
                .build();

        given(memberRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        SignUpResponse response = memberService.signUp(request);

        // then
        assertNotNull(response);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void 이메일_중복_예외() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("duplicate@example.com")
                .password("password")
                .name("중복유저")
                .build();

        given(memberRepository.existsByEmail(request.getEmail())).willReturn(true);

        // when & then
        assertThrows(EmailAlreadyExistException.class, () -> memberService.signUp(request));
    }

    @Test
    void 로그인_비밀번호_불일치_예외() {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("wrongPassword")
                .build();

        Member fakeMember = Member.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(fakeMember));
        given(passwordEncoder.matches(request.getPassword(), fakeMember.getPassword())).willReturn(false);

        // when & then
        assertThrows(PasswordNotMatchedException.class, () -> memberService.login(request));
    }

    @Test
    void 로그인_이메일없음_예외() {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("notfound@example.com")
                .password("anyPassword")
                .build();

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> memberService.login(request));
    }

    @Test
    void 로그인_성공_JWT_토큰생성() {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("user@example.com")
                .password("password123")
                .build();

        Member member = Member.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .build();

        JwtToken dummyToken = JwtToken.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(request.getPassword(), member.getPassword())).willReturn(true);
        given(jwtUtils.generateToken(any())).willReturn(dummyToken);

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        Authentication authentication = mock(Authentication.class);
        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);

        // when
        LoginResponse response = memberService.login(request);

        // then
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        verify(jwtUtils).generateToken(any());
    }

}

