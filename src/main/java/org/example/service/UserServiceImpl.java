package org.example.service;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.domain.enums.Role;
import org.example.dto.UserCreateDto;
import org.example.dto.UserUpdateDto;
import org.example.repository.UserDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(UserCreateDto dto) {
        if (userDao.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException(
                    "User with username '" + dto.username() + "' already exists");
        }
        if (userDao.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException(
                    "User with email '" + dto.email() + "' already exists");
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());
        return userDao.save(user);
    }
    @Override
    public Optional<User> getUserById(UUID id) {
        return userDao.findById(id);
    }
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
    @Override
    public List<User> getUsersByRole(Role role) {
        return userDao.findByRole(role);
    }
    @Override
    public List<User> getActiveUsers() {
        return userDao.findAllActive();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(UUID id, UserUpdateDto dto) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        if (dto.username() != null && !dto.username().isBlank()
                && !dto.username().equals(user.getUsername())) {
            if (userDao.findByUsername(dto.username()).isPresent()) {
                throw new IllegalArgumentException("Username already taken");
            }
            user.setUsername(dto.username());
        }
        if (dto.email() != null && !dto.email().isBlank()
                && !dto.email().equals(user.getEmail())) {
            if (userDao.findByEmail(dto.email()).isPresent()) {
                throw new IllegalArgumentException("Email already taken");
            }
            user.setEmail(dto.email());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }
        if (dto.role() != null) {
            user.setRole(dto.role());
        }
        return userDao.update(user);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(UUID id) {
        if (!userDao.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        userDao.deleteById(id);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteUser(UUID id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setDeleted(true);   
        userDao.update(user);    
    }
}