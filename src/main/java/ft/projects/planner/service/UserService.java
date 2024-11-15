package ft.projects.planner.service;

import ft.projects.planner.model.UserRequest;
import ft.projects.planner.model.UserResponse;

import java.util.UUID;

public interface UserService {

    UUID register(UserRequest userRequest);
    UserResponse login();
}
