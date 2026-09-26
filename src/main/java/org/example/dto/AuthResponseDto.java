package org.example.dto;
public record AuthResponseDto(String accessToken, String refreshToken, UserResponseDto user) {}