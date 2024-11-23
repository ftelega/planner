package ft.projects.planner.controller;

import ft.projects.planner.exception.PlannerExceptionResponse;
import ft.projects.planner.model.CreatePlanEntryResponse;
import ft.projects.planner.model.PlanEntryRequest;
import ft.projects.planner.model.PlanEntryResponse;
import ft.projects.planner.service.PlanEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/plan-entries")
@RequiredArgsConstructor
@Tag(name = "plan-entry", description = "Plan Entries")
public class PlanEntryController {

    private final PlanEntryService planEntryService;

    @Operation(summary = "Create Plan Entry", description = "Create User's Plan Entry", tags = { "plan-entry" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created user's plan entry", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePlanEntryResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PlannerExceptionResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid Csrf Token", content = {  @Content() }),
            @ApiResponse(responseCode = "401", description = "Unauthorized, please login with the login endpoint first", content = {  @Content() })
        }
    )
    @Parameter(name = "X-XSRF-TOKEN", description = "Csrf Token Header", required = true, in = ParameterIn.HEADER, allowEmptyValue = true)
    @PostMapping(path = "/create")
    public ResponseEntity<CreatePlanEntryResponse> createPlanEntry(@RequestBody PlanEntryRequest planEntryRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planEntryService.createPlanEntry(planEntryRequest));
    }

    @Operation(summary = "Get Plan Entries", description = "Get User's Plan Entries", tags = { "plan-entry" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched user's plan entries", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PlanEntryResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PlannerExceptionResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized, please login with the login endpoint first", content = {  @Content() })
        }
    )
    @GetMapping
    public ResponseEntity<List<PlanEntryResponse>> getAllPlanEntries() {
        return ResponseEntity.status(HttpStatus.OK).body(planEntryService.getAllPlanEntries());
    }
}
