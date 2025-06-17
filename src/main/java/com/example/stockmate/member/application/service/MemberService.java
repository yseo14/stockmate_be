package com.example.stockmate.member.application.service;

import com.example.stockmate.member.Exception.EmailAlreadyExistException;
import com.example.stockmate.member.application.mapper.SignUpMapper;
import com.example.stockmate.member.domain.Member;
import com.example.stockmate.member.dto.SignUpRequest;
import com.example.stockmate.member.dto.SignUpResponse;
import com.example.stockmate.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (memberRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        String providerId = UUID.randomUUID().toString();
        Member member = SignUpMapper.toMember(signUpRequest, encodedPassword, providerId);
        memberRepository.save(member);

        return SignUpMapper.toSignUpResponse(member);
    }
}
