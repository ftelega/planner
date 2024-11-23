package ft.projects.planner.service;

import ft.projects.planner.model.CreatePlanEntryResponse;
import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.PlanEntryResponse;

import java.util.List;

public interface PlanEntryService {

    CreatePlanEntryResponse createPlanEntry(PlanEntryRequest planEntryRequest);
    List<PlanEntryResponse> getAllPlanEntries();
}
