package org.example.repository;
import org.example.domain.Programmer;
import org.example.domain.enums.EmploymentType;
import org.example.domain.enums.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface ProgrammerRepository extends JpaRepository<Programmer, UUID> {
    List<Programmer> findByProjectProjectId(UUID projectId);
    List<Programmer> findByLastNameIgnoreCase(String lastName);
    List<Programmer> findByPosition(Position position);
    List<Programmer> findByEmploymentType(EmploymentType type);
    Long countByProjectProjectId(UUID projectId);
}