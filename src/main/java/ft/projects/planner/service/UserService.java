package ft.projects.planner.service;

import ft.projects.planner.model.UserRequest;
import ft.projects.planner.model.UserResponse;

public interface UserService {

    UserResponse register(UserRequest userRequest);
    UserResponse login();
}
