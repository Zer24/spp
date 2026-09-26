package org.example.repository;
import lombok.RequiredArgsConstructor;
import org.example.domain.Programmer;
import org.example.domain.enums.EmploymentType;
import org.example.domain.enums.Position;
import org.example.repository.ProgrammerRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
@RequiredArgsConstructor
public class ProgrammerDaoImpl implements ProgrammerDao {
    private final ProgrammerRepository repository;
    @Override
    public Programmer save(Programmer e) { return repository.save(e); }
    @Override
    public Optional<Programmer> findById(UUID id) { return repository.findById(id); }
    @Override
    public List<Programmer> findAll() { return repository.findAll(); }
    @Override
    public Programmer update(Programmer e) { e.setSalary(e.calculateSalary()); return repository.save(e); }
    @Override
    public void deleteById(UUID id) { repository.deleteById(id); }
    @Override
    public boolean existsById(UUID id) { return repository.existsById(id); }
    @Override
    public List<Programmer> findByProjectId(UUID projectId) {
        return repository.findByProjectProjectId(projectId);
    }
    @Override
    public List<Programmer> findByLastName(String lastName) {
        return repository.findByLastNameIgnoreCase(lastName);
    }
    @Override
    public List<Programmer> findByPosition(Position position) {
        return repository.findByPosition(position);
    }
    @Override
    public List<Programmer> findFullTime() {
        return repository.findByEmploymentType(EmploymentType.FULL_TIME);
    }
    @Override
    public void flush() {
        repository.flush();
    }
}