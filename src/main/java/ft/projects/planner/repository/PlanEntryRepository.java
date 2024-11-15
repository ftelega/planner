package ft.projects.planner.repository;

import ft.projects.planner.model.PlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanEntryRepository extends JpaRepository<PlanEntry, UUID> {

}
