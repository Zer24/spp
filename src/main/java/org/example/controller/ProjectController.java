package org.example.controller;
import lombok.RequiredArgsConstructor;
import org.example.domain.Project;
import org.example.dto.ProjectDto;
import org.example.dto.ProjectProgrammerAssignmentDto;
import org.example.service.ProjectService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    @GetMapping
    public List<ProjectDto> getAll() {
        return projectService.getAllProjects().stream().map(ProjectDto::from).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getById(@PathVariable UUID id) {
        return projectService.getProjectById(id)
                .map(ProjectDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
    public List<ProjectDto> search(@RequestParam String name) {
        return projectService.searchProjectsByName(name).stream()
                .map(ProjectDto::from).toList();
    }
    @GetMapping("/by-date")
    public List<ProjectDto> byDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return projectService.getProjectsByDateRange(from, to).stream()
                .map(ProjectDto::from).toList();
    }
    @GetMapping("/{id}/programmers-count")
    public long programmersCount(@PathVariable UUID id) {
        return projectService.countProgrammersOnProject(id);
    }
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ProjectDto create(@RequestBody ProjectDto dto) {
        Project project = new Project(dto.name(), dto.customer(), dto.startDate(), dto.endDate());
        return ProjectDto.from(projectService.createProject(project));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ProjectDto update(@PathVariable UUID id, @RequestBody ProjectDto dto) {
        Project project = new Project(dto.name(), dto.customer(), dto.startDate(), dto.endDate());
        project.setProjectId(id);
        return ProjectDto.from(projectService.updateProject(project));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/recalculate")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> recalculate(@PathVariable UUID id) {
        projectService.recalculateProjectTotals(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{id}/programmers")
    @PreAuthorize("hasRole('MANAGER')")
    public ProjectDto assignProgrammers(@PathVariable UUID id,
                                        @RequestBody List<ProjectProgrammerAssignmentDto> assignments) {
        return ProjectDto.from(projectService.assignProgrammers(id, assignments));
    }
}