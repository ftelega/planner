package ft.projects.planner.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "plan-entries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Uuid;
    private String content;
    private LocalDate date;
    @ManyToOne
    private User user;
}
