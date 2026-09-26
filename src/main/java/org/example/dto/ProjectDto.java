package org.example.dto;
import org.example.domain.Project;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
public record ProjectDto(
        UUID projectId,
        String name,
        String customer,
        LocalDate startDate,
        LocalDate endDate,
        int programmersCount,
        BigDecimal cost
) {
    public static ProjectDto from(Project p) {
        return new ProjectDto(
                p.getProjectId(),
                p.getName(),
                p.getCustomer(),
                p.getStartDate(),
                p.getEndDate(),
                p.getProgrammersCount(),
                p.getCost()
        );
    }
}