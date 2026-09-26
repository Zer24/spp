package org.example.domain;
import jakarta.persistence.*;
import lombok.*;
import org.example.domain.enums.EmploymentType;
import org.example.domain.enums.Position;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
@Entity
@Table(name = "programmers", indexes = {
        @Index(name = "idx_programmers_project", columnList = "project_id"),
        @Index(name = "idx_programmers_last_name", columnList = "last_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "project")
public class Programmer {
    private static final BigDecimal TAX_MULTIPLIER = new BigDecimal("1.77");
    private static final int FULL_TIME_HOURS = 8;
    private static final int PART_TIME_HOURS = 4;
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "programmer_id", updatable = false, nullable = false)
    private UUID programmerId;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;
    @Column(name = "last_name",  nullable = false, length = 64)
    private String lastName;
    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;
    @Column(name = "middle_name", length = 64)
    private String middleName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Position position;
    @Column(name = "work_start_date", nullable = true)
    private LocalDate workStartDate;
    @Column(name = "work_end_date", nullable = true)
    private LocalDate workEndDate;
    @Column(name = "hourly_rate", nullable = false, precision = 19, scale = 2)
    private BigDecimal hourlyRate;
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 32)
    private EmploymentType employmentType;
    @Column(name = "salary", nullable = false, precision = 19, scale = 2)
    private BigDecimal salary = BigDecimal.ZERO;
    @PrePersist
    @PreUpdate
    public void preSave() {
        if (employmentType == null) {
            throw new IllegalStateException("employmentType must not be null");
        }
        if (position == null) {
            throw new IllegalStateException("position must not be null");
        }
        if (hourlyRate == null || hourlyRate.signum() < 0) {
            throw new IllegalStateException("hourlyRate must be >= 0");
        }
        if (project == null) {
            this.salary = BigDecimal.ZERO;
            return;
        }
        validateDates();
        this.salary = calculateSalary();
    }
    private void validateDates() {
        if (project.getStartDate() == null || project.getEndDate() == null) {
            throw new IllegalStateException("Project must have start/end dates");
        }
        if (workStartDate == null || workEndDate == null) {
            throw new IllegalStateException("Work dates must be set when project is assigned");
        }
        if (workStartDate.isBefore(project.getStartDate())) {
            throw new IllegalStateException("workStartDate must be >= project.startDate");
        }
        if (workEndDate.isBefore(workStartDate)) {
            throw new IllegalStateException("workEndDate must not be before workStartDate");
        }
        if (workEndDate.isAfter(project.getEndDate())) {
            throw new IllegalStateException("workEndDate must be <= project.endDate");
        }
    }
    public BigDecimal calculateSalary() {
        if (hourlyRate == null || workStartDate == null || workEndDate == null
                || employmentType == null) {
            return BigDecimal.ZERO;
        }
        long workingDays = countWorkingDays(workStartDate, workEndDate);
        int hoursPerDay = employmentType == EmploymentType.FULL_TIME
                ? FULL_TIME_HOURS
                : PART_TIME_HOURS;
        BigDecimal hours = BigDecimal.valueOf(workingDays * hoursPerDay);
        BigDecimal gross = hourlyRate.multiply(hours);
        return gross.multiply(TAX_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
    }
    private static long countWorkingDays(LocalDate from, LocalDate to) {
        long days = ChronoUnit.DAYS.between(from, to) + 1;
        long fullWeeks = days / 7;
        long workingDays = fullWeeks * 5;
        long remainder = days % 7;
        DayOfWeek start = from.getDayOfWeek();
        for (int i = 0; i < remainder; i++) {
            DayOfWeek d = start.plus(i);
            if (d != DayOfWeek.SATURDAY && d != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }
        return workingDays;
    }
    public void assignToProject(Project project,
                                LocalDate workStartDate,
                                LocalDate workEndDate) {
        this.project = project;
        this.workStartDate = workStartDate;
        this.workEndDate = workEndDate;
        validateDates();
        this.salary = calculateSalary();
        if (project != null && !project.getProgrammers().contains(this)) {
            project.getProgrammers().add(this);
        }
    }
    public void unassignProject() {
        if (this.project != null) {
            this.project.getProgrammers().remove(this);
        }
        this.project = null;
        this.workStartDate = null;
        this.workEndDate = null;
        this.salary = BigDecimal.ZERO;
    }
    public Programmer(Project project,
                      String lastName, String firstName, String middleName,
                      Position position,
                      LocalDate workStartDate, LocalDate workEndDate,
                      BigDecimal hourlyRate, EmploymentType employmentType) {
        this.project = project;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.position = position;
        this.workStartDate = workStartDate;
        this.workEndDate = workEndDate;
        this.hourlyRate = hourlyRate;
        this.employmentType = employmentType;
    }
    public Programmer(String lastName, String firstName, String middleName,
                      Position position,
                      BigDecimal hourlyRate, EmploymentType employmentType) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.position = position;
        this.hourlyRate = hourlyRate;
        this.employmentType = employmentType;
    }
}