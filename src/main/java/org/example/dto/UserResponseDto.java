package org.example.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.domain.User;
import org.example.domain.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;
@Schema(description = "DTO с данными пользователя для ответа")
public record UserResponseDto(
        @Schema(description = "Уникальный идентификатор пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,
        @Schema(description = "Имя пользователя", example = "hanna")
        String username,
        @Schema(description = "Email пользователя", example = "example@example.com")
        String email,
        @Schema(description = "Роль пользователя", example = "CUSTOMER")
        Role role,
        boolean isDeleted,
        LocalDateTime createdAt
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isDeleted(),
                user.getCreatedAt()
        );
    }
}