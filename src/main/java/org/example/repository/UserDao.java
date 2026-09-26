package org.example.repository;
import org.example.domain.User;
import org.example.domain.enums.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface UserDao extends GenericDao<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User>    findByRole(Role role);
    List<User>    findAllActive();
    void          softDelete(UUID userId);
}