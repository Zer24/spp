package org.example.service;
import org.example.domain.Project;
import org.example.dto.ProjectProgrammerAssignmentDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface ProjectService {
    Project createProject(Project project);
    Optional<Project> getProjectById(UUID id);
    List<Project> getAllProjects();
    Project updateProject(Project project);
    void deleteProject(UUID id);
    List<Project> searchProjectsByName(String fragment);
    List<Project> getProjectsByDateRange(LocalDate from, LocalDate to);
    long countProgrammersOnProject(UUID projectId);
    void recalculateProjectTotals(UUID projectId);
    public Project assignProgrammers(UUID projectId, List<ProjectProgrammerAssignmentDto> assignments);
}