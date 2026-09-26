package org.example.controller;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto req) {
        return ResponseEntity.ok(authService.login(req));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshRequestDto req) {
        return ResponseEntity.ok(authService.refresh(req.refreshToken()));
    }
}