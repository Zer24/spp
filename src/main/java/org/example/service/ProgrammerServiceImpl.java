package org.example.service;
import lombok.RequiredArgsConstructor;
import org.example.domain.Programmer;
import org.example.domain.enums.Position;
import org.example.repository.ProgrammerDao;
import org.example.repository.ProgrammerDaoImpl;
import org.example.service.ProgrammerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgrammerServiceImpl implements ProgrammerService {
    private final ProgrammerDao programmerDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Programmer createProgrammer(Programmer programmer) {
        programmer.setSalary(programmer.calculateSalary());
        return programmerDao.save(programmer);
    }
    @Override
    public Optional<Programmer> getProgrammerById(UUID id) {
        return programmerDao.findById(id);
    }
    @Override
    public List<Programmer> getAllProgrammers() {
        return programmerDao.findAll();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Programmer updateProgrammer(Programmer programmer) {
        if (!programmerDao.existsById(programmer.getProgrammerId())) {
            throw new IllegalArgumentException(
                    "Programmer not found: " + programmer.getProgrammerId());
        }
        return programmerDao.update(programmer);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProgrammer(UUID id) {
        Programmer programmer = programmerDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Programmer not found: " + id));
        programmer.unassignProject();  
        programmerDao.deleteById(id);
    }
    @Override
    public List<Programmer> getProgrammersByProject(UUID projectId) {
        return programmerDao.findByProjectId(projectId);
    }
    @Override
    public List<Programmer> getProgrammersByLastName(String lastName) {
        return programmerDao.findByLastName(lastName);
    }
    @Override
    public List<Programmer> getProgrammersByPosition(Position position) {
        return programmerDao.findByPosition(position);
    }
    @Override
    public List<Programmer> getFullTimeProgrammers() {
        return programmerDao.findFullTime();
    }
}