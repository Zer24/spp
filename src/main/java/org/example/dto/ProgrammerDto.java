package org.example.dto;
import org.example.domain.Programmer;
import org.example.domain.enums.EmploymentType;
import org.example.domain.enums.Position;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
public record ProgrammerDto(
        UUID programmerId,
        UUID projectId,
        String projectName,
        String lastName,
        String firstName,
        String middleName,
        Position position,
        LocalDate workStartDate,
        LocalDate workEndDate,
        BigDecimal hourlyRate,
        EmploymentType employmentType,
        BigDecimal salary
) {
    public static ProgrammerDto from(Programmer p) {
        return new ProgrammerDto(
                p.getProgrammerId(),
                p.getProject() != null ? p.getProject().getProjectId() : null,
                p.getProject() != null ? p.getProject().getName() : null,
                p.getLastName(), p.getFirstName(), p.getMiddleName(),
                p.getPosition(), p.getWorkStartDate(), p.getWorkEndDate(),
                p.getHourlyRate(), p.getEmploymentType(), p.getSalary()
        );
    }
}