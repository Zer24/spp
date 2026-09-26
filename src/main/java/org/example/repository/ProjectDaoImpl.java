package org.example.repository;
import lombok.RequiredArgsConstructor;
import org.example.domain.Programmer;
import org.example.domain.Project;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
@RequiredArgsConstructor
public class ProjectDaoImpl implements ProjectDao {
    private final ProjectRepository repository;
    private final ProgrammerRepository programmerRepository;
    @Override
    public Project save(Project e) { return repository.save(e); }
    @Override
    public Optional<Project> findById(UUID id) { return repository.findById(id); }
    @Override
    public List<Project> findAll() { return repository.findAll(); }
    @Override
    public Project update(Project e) { return repository.save(e); }
    @Override
    public void deleteById(UUID id) { repository.deleteById(id); }
    @Override
    public boolean existsById(UUID id) { return repository.existsById(id); }
    @Override
    public List<Project> findByNameContaining(String fragment) {
        return repository.findByNameContainingIgnoreCase(fragment);
    }
    @Override
    public List<Project> findByStartDateBetween(LocalDate from, LocalDate to) {
        return repository.findByStartDateBetween(from, to);
    }
    @Override
    public long countProgrammers(UUID projectId) {
        return programmerRepository.countByProjectProjectId(projectId);
    }
    @Override
    public void recalculateTotals(UUID projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));
        List<Programmer> programmers = programmerRepository.findByProjectProjectId(projectId);
        programmers.forEach(p -> p.setSalary(p.calculateSalary()));
        BigDecimal total = programmers.stream()
                .map(Programmer::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        project.setProgrammersCount(programmers.size());
        project.setCost(total.multiply(BigDecimal.valueOf(2)));
        repository.save(project);
    }
}