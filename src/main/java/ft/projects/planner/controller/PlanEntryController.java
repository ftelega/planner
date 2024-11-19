package ft.projects.planner.controller;

import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.PlanEntryResponse;
import ft.projects.planner.service.PlanEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/plan-entries")
@RequiredArgsConstructor
public class PlanEntryController {

    private final PlanEntryService planEntryService;

    @PostMapping(path = "/create")
    public ResponseEntity<UUID> createPlanEntry(@RequestBody PlanEntryRequest planEntryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planEntryService.createPlanEntry(planEntryRequest));
    }

    @GetMapping
    public ResponseEntity<List<PlanEntryResponse>> getAllPlanEntries() {
        return ResponseEntity.status(HttpStatus.OK).body(planEntryService.getAllPlanEntries());
    }
}
