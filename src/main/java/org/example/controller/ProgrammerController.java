package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.Programmer;
import org.example.domain.Project;
import org.example.domain.enums.Position;
import org.example.dto.ProgrammerDto;
import org.example.service.ProgrammerService;
import org.example.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/programmers")
@RequiredArgsConstructor
public class ProgrammerController {
    private final ProgrammerService programmerService;
    private final ProjectService projectService;
    @GetMapping
    public List<ProgrammerDto> getAll() {
        return programmerService.getAllProgrammers().stream().map(ProgrammerDto::from).toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProgrammerDto> getById(@PathVariable UUID id) {
        return programmerService.getProgrammerById(id)
                .map(ProgrammerDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/by-project/{projectId}")
    public List<ProgrammerDto> byProject(@PathVariable UUID projectId) {
        return programmerService.getProgrammersByProject(projectId).stream().map(ProgrammerDto::from).toList();
    }
    @GetMapping("/by-last-name")
    public List<ProgrammerDto> byLastName(@RequestParam String lastName) {
        return programmerService.getProgrammersByLastName(lastName).stream().map(ProgrammerDto::from).toList();
    }
    @GetMapping("/by-position")
    public List<ProgrammerDto> byPosition(@RequestParam Position position) {
        return programmerService.getProgrammersByPosition(position).stream().map(ProgrammerDto::from).toList();
    }
    @GetMapping("/full-time")
    public List<ProgrammerDto> fullTime() {
        return programmerService.getFullTimeProgrammers().stream().map(ProgrammerDto::from).toList();
    }
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ProgrammerDto create(@RequestBody ProgrammerDto dto) {
        Programmer p = buildProgrammer(dto, null);
        return ProgrammerDto.from(programmerService.createProgrammer(p));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ProgrammerDto update(@PathVariable UUID id, @RequestBody ProgrammerDto dto) {
        Programmer existing = programmerService.getProgrammerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Programmer not found"));
        Programmer p = buildProgrammer(dto, existing.getProgrammerId());
        return ProgrammerDto.from(programmerService.updateProgrammer(p));
    }
    private Programmer buildProgrammer(ProgrammerDto dto, UUID id) {
        Project project = null;
        if (dto.projectId() != null) {
            project = projectService.getProjectById(dto.projectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        }
        Programmer p = new Programmer(
                dto.lastName(), dto.firstName(), dto.middleName(),
                dto.position(), dto.hourlyRate(), dto.employmentType());
        if (project != null) {
            p.assignToProject(project, dto.workStartDate(), dto.workEndDate());
        } else {
            p.setWorkStartDate(null);
            p.setWorkEndDate(null);
        }
        if (id != null) p.setProgrammerId(id);
        return p;
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        programmerService.deleteProgrammer(id);
        return ResponseEntity.noContent().build();
    }
}