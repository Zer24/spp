package org.example.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.dto.UserCreateDto;
import org.example.dto.UserResponseDto;
import org.example.dto.UserUpdateDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<UserResponseDto> getAll() {
        return userService.getAllUsers().stream().map(UserResponseDto::from).toList();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UserResponseDto> getById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(UserResponseDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UserResponseDto create(@Valid @RequestBody UserCreateDto dto) {
        User created = userService.registerUser(dto);
        return UserResponseDto.from(created);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UserResponseDto update(@PathVariable UUID id,
                                  @Valid @RequestBody UserUpdateDto dto) {
        return UserResponseDto.from(userService.updateUser(id, dto));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);   
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> softDelete(@PathVariable UUID id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}