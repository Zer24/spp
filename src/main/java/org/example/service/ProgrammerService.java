package org.example.service;
import org.example.domain.Programmer;
import org.example.domain.enums.Position;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface ProgrammerService {
    Programmer createProgrammer(Programmer programmer);
    Optional<Programmer> getProgrammerById(UUID id);
    List<Programmer> getAllProgrammers();
    Programmer updateProgrammer(Programmer programmer);
    void deleteProgrammer(UUID id);
    List<Programmer> getProgrammersByProject(UUID projectId);
    List<Programmer> getProgrammersByLastName(String lastName);
    List<Programmer> getProgrammersByPosition(Position position);
    List<Programmer> getFullTimeProgrammers();
}