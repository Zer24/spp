package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.dto.AuthRequestDto;
import org.example.dto.AuthResponseDto;
import org.example.dto.UserResponseDto;
import org.example.repository.UserDao;
import org.example.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDao userDao;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    @Transactional(readOnly = true)
    public AuthResponseDto login(AuthRequestDto req) {
        User user = userDao.findByUsername(req.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (user.isDeleted() || !passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        UserResponseDto dto = UserResponseDto.from(user);
        return new AuthResponseDto(
                tokenProvider.generateAccessToken(dto),
                tokenProvider.generateRefreshToken(dto),
                dto
        );
    }
    @Transactional(readOnly = true)
    public AuthResponseDto refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)
                || !"refresh".equals(tokenProvider.getTokenType(refreshToken))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        var userId = tokenProvider.getUserIdFromToken(refreshToken);
        User user = userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserResponseDto dto = UserResponseDto.from(user);
        return new AuthResponseDto(
                tokenProvider.generateAccessToken(dto),
                tokenProvider.generateRefreshToken(dto),
                dto
        );
    }
}