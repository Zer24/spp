package org.example.dto;
import java.time.LocalDate;
import java.util.UUID;
public record ProjectProgrammerAssignmentDto(
        UUID programmerId,
        LocalDate workStartDate,
        LocalDate workEndDate
) {
}
