package ft.projects.planner.controller;

import ft.projects.planner.model.UserRequest;
import ft.projects.planner.model.UserResponse;
import ft.projects.planner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<UUID> register(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRequest));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserResponse> login() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login());
    }
}
