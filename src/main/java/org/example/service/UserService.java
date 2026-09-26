package org.example.service;
import org.example.domain.User;
import org.example.domain.enums.Role;
import org.example.dto.UserCreateDto;
import org.example.dto.UserUpdateDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface UserService {
    User registerUser(UserCreateDto dto);
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> getUsersByRole(Role role);
    List<User> getActiveUsers();
    User updateUser(UUID id, UserUpdateDto dto);
    void deleteUser(UUID id);
    void softDeleteUser(UUID id);
}