package org.example.repository;
import org.example.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByNameContainingIgnoreCase(String fragment);
    List<Project> findByStartDateBetween(LocalDate from, LocalDate to);
}