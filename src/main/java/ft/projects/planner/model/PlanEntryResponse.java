package ft.projects.planner.model;

import java.time.LocalDate;

public record PlanEntryResponse(
        String uuid,
        String content,
        LocalDate date
) {
}
