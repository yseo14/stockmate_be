package com.example.stockmate.member.application.service;

import com.example.stockmate.global.enums.Role;
import com.example.stockmate.member.domain.Member;
import com.example.stockmate.member.dto.SignUpRequestDto;
import com.example.stockmate.member.repository.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDto request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member newMember = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .provider("local")
                .providerId(UUID.randomUUID().toString())
                .role(Role.USER)
                .build();

        memberRepository.save(newMember);
    }
}
