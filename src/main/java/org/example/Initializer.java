package org.example;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.domain.enums.Role;
import org.example.repository.ProgrammerRepository;
import org.example.repository.ProjectDao;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ProgrammerRepository programmerRepository;
    private final ProjectDao projectDao;
    private final PasswordEncoder passwordEncoder;
    @Value("${ADMIN_USERNAME:admin}")
    private String adminUsername;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;
    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;
    @Override
    public void run(String... args) {
        if (!userRepository.findByUsername(adminUsername).isPresent()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setRole(Role.ADMINISTRATOR);
            userRepository.save(admin);
        }
    }
}