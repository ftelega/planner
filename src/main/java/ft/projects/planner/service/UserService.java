package ft.projects.planner.service;

import ft.projects.planner.model.RegisterResponse;
import ft.projects.planner.model.UserRequest;
import ft.projects.planner.model.UserResponse;

public interface UserService {

    RegisterResponse register(UserRequest userRequest);
    UserResponse login();
}
