package ft.projects.planner.service;

import ft.projects.planner.model.User;

public interface SecurityService {

    User getCurrentUserFromAuthentication();
}
