package org.example.repository;
import org.example.domain.User;
import org.example.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User>    findByRole(Role role);
    List<User>    findByIsDeletedFalse();
    @Modifying
    @Query("update User u set u.isDeleted = true where u.userId = :id")
    void softDelete(@Param("id") UUID id);
}