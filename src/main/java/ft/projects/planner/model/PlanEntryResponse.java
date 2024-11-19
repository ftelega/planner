package ft.projects.planner.model;

import java.time.LocalDate;

public record PlanEntryResponse(
        String content,
        LocalDate date
) {
}
