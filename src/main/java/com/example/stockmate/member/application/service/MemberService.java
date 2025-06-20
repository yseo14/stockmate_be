package com.example.stockmate.member.application.service;

import com.example.stockmate.global.jwt.JwtToken;
import com.example.stockmate.global.jwt.JwtUtils;
import com.example.stockmate.global.redis.RedisDao;
import com.example.stockmate.member.Exception.EmailAlreadyExistException;
import com.example.stockmate.member.Exception.MemberNotFoundException;
import com.example.stockmate.member.Exception.PasswordNotMatchedException;
import com.example.stockmate.member.application.mapper.LoginMapper;
import com.example.stockmate.member.application.mapper.LogoutMapper;
import com.example.stockmate.member.application.mapper.SignUpMapper;
import com.example.stockmate.member.domain.Member;
import com.example.stockmate.member.dto.LoginRequest;
import com.example.stockmate.member.dto.LoginResponse;
import com.example.stockmate.member.dto.LogoutResponse;
import com.example.stockmate.member.dto.SignUpRequest;
import com.example.stockmate.member.dto.SignUpResponse;
import com.example.stockmate.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisDao redisDao;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String providerId = UUID.randomUUID().toString();
        Member member = SignUpMapper.toMember(request, encodedPassword, providerId);
        memberRepository.save(member);

        return SignUpMapper.toSignUpResponse(member);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail();
        String rawPassword = request.getPassword();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new PasswordNotMatchedException();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, rawPassword);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtUtils.generateToken(authentication);

        return LoginMapper.toLoginResponse(member, jwtToken);
    }

    public LogoutResponse logout(HttpServletRequest request) {
        String accessToken = jwtUtils.resolveToken(request);
        String email = jwtUtils.getUserNameFromToken(accessToken);
        jwtUtils.deleteRefreshToken(email);

        long expiration = jwtUtils.getRemainingExpiration(accessToken);
        redisDao.setValues("blacklist:" + accessToken, "logout", Duration.ofMillis(expiration));
        return LogoutMapper.toLogoutResponse();
    }
}
