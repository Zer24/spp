package org.example.domain;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_projects_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"programmers"})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "project_id", updatable = false, nullable = false)
    private UUID projectId;
    @Column(nullable = false, length = 256)
    private String name;
    @Column(nullable = false, length = 256)
    private String customer;
    @Column(name = "programmers_count", nullable = false)
    private int programmersCount = 0;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "cost", nullable = false, precision = 19, scale = 2)
    private BigDecimal cost = BigDecimal.ZERO;
    @OneToMany(mappedBy = "project")
    private List<Programmer> programmers = new ArrayList<>();
    @PrePersist
    @PreUpdate
    public void validate() {
        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new IllegalStateException("endDate must be after startDate");
        }
    }
    public Project(String name, String customer, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.customer=customer;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}