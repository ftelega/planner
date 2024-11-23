package ft.projects.planner.controller;

import ft.projects.planner.exception.PlannerExceptionResponse;
import ft.projects.planner.model.RegisterResponse;
import ft.projects.planner.model.UserRequest;
import ft.projects.planner.model.UserResponse;
import ft.projects.planner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
@Tag(name = "user", description = "User API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register", description = "Register User", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PlannerExceptionResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid Csrf Token", content = {  @Content() })
        }
    )
    @Parameter(name = "X-XSRF-TOKEN", description = "Csrf Token Header", required = true, in = ParameterIn.HEADER, allowEmptyValue = true)
    @PostMapping(path = "/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRequest));
    }

    @Operation(summary = "Login", description = "Login User", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PlannerExceptionResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Invalid Csrf Token", content = {  @Content() }),
            @ApiResponse(responseCode = "401", description = "Invalid Basic Authentication", content = {  @Content() })
        }
    )
    @Parameter(name = "X-XSRF-TOKEN", description = "Csrf Token Header", required = true, in = ParameterIn.HEADER, allowEmptyValue = true)
    @SecurityRequirement(name = "BasicAuth")
    @PostMapping(path = "/login")
    public ResponseEntity<UserResponse> login() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login());
    }
}
