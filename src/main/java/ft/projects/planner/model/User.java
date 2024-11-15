package ft.projects.planner.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Uuid;
    private String username;
    private String password;
    @OneToMany(mappedBy = "user")
    private List<PlanEntry> planEntries;
}
