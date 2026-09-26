package org.example.repository;

import org.example.domain.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public interface ProjectDao extends GenericDao<Project, UUID> {
    List<Project> findByNameContaining(String fragment);
    List<Project> findByStartDateBetween(LocalDate from, LocalDate to);
    long countProgrammers(UUID projectId);
    void recalculateTotals(UUID projectId);
}