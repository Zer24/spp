package org.example.repository;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.domain.enums.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final UserRepository repository;
    @Override
    public User save(User entity) { return repository.save(entity); }
    @Override
    public Optional<User> findById(UUID id) { return repository.findById(id); }
    @Override
    public List<User> findAll() { return repository.findAll(); }
    @Override
    public User update(User entity) { return repository.save(entity); }
    @Override
    public void deleteById(UUID id) { repository.deleteById(id); }
    @Override
    public boolean existsById(UUID id) { return repository.existsById(id); }
    @Override
    public Optional<User> findByUsername(String username) { return repository.findByUsername(username); }
    @Override
    public Optional<User> findByEmail(String email) { return repository.findByEmail(email); }
    @Override
    public List<User> findByRole(Role role) { return repository.findByRole(role); }
    @Override
    public List<User> findAllActive() { return repository.findByIsDeletedFalse(); }
    @Override
    public void softDelete(UUID userId) { repository.softDelete(userId); }
}