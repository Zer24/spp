package org.example.repository;
import org.example.domain.Programmer;
import org.example.domain.enums.Position;
import java.util.List;
import java.util.UUID;
public interface ProgrammerDao extends GenericDao<Programmer, UUID> {
    List<Programmer> findByProjectId(UUID projectId);
    List<Programmer> findByLastName(String lastName);
    List<Programmer> findByPosition(Position position);
    List<Programmer> findFullTime();
    void flush();
}