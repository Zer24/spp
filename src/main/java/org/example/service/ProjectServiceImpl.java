package org.example.service;
import lombok.RequiredArgsConstructor;
import org.example.domain.Programmer;
import org.example.domain.Project;
import org.example.dto.ProjectProgrammerAssignmentDto;
import org.example.repository.ProgrammerDao;
import org.example.repository.ProjectDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDao projectDao;
    private final ProgrammerDao programmerDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Project createProject(Project project) {
        return projectDao.save(project);
    }
    @Override
    public Optional<Project> getProjectById(UUID id) {
        return projectDao.findById(id);
    }
    @Override
    public List<Project> getAllProjects() {
        return projectDao.findAll();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Project updateProject(Project project) {
        if (!projectDao.existsById(project.getProjectId())) {
            throw new IllegalArgumentException(
                    "Project not found: " + project.getProjectId());
        }
        return projectDao.update(project);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProject(UUID id) {
        Project project = projectDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        for (Programmer p : programmerDao.findByProjectId(id)) {
            p.unassignProject();          
        }
        programmerDao.flush();            
        projectDao.deleteById(id);
    }
    @Override
    public List<Project> searchProjectsByName(String fragment) {
        return projectDao.findByNameContaining(fragment);
    }
    @Override
    public List<Project> getProjectsByDateRange(LocalDate from, LocalDate to) {
        return projectDao.findByStartDateBetween(from, to);
    }
    @Override
    public long countProgrammersOnProject(UUID projectId) {
        return projectDao.countProgrammers(projectId);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recalculateProjectTotals(UUID projectId) {
        projectDao.recalculateTotals(projectId);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Project assignProgrammers(UUID projectId,
                                     List<ProjectProgrammerAssignmentDto> assignments) {
        Project project = projectDao.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Map<UUID, ProjectProgrammerAssignmentDto> byId = assignments.stream()
                .collect(Collectors.toMap(ProjectProgrammerAssignmentDto::programmerId, a -> a));
        for (Programmer p : programmerDao.findByProjectId(projectId)) {
            if (!byId.containsKey(p.getProgrammerId())) {
                p.unassignProject();
            }
        }
        for (ProjectProgrammerAssignmentDto a : assignments) {
            Programmer p = programmerDao.findById(a.programmerId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Programmer not found: " + a.programmerId()));
            p.assignToProject(project, a.workStartDate(), a.workEndDate());
        }
        programmerDao.flush();
        recalculateProjectTotals(projectId);
        return projectDao.findById(projectId).orElseThrow();
    }
}