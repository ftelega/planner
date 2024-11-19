package ft.projects.planner.service;

import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.PlanEntryResponse;

import java.util.List;
import java.util.UUID;

public interface PlanEntryService {

    UUID createPlanEntry(PlanEntryRequest planEntryRequest);
    List<PlanEntryResponse> getAllPlanEntries();
}
