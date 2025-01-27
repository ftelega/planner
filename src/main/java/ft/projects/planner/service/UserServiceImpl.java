package ft.projects.planner.service;

import ft.projects.planner.exception.Exceptions;
import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.User;
import ft.projects.planner.model.UserRequest;
import ft.projects.planner.model.UserResponse;
import ft.projects.planner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    @Override
    public UserResponse register(UserRequest userRequest) {
        var username = userRequest.username();
        var password = userRequest.password();
        validateUserExistence(username);
        validateUsername(username);
        validatePassword(password);
        var user = userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .build()
        );
        return new UserResponse(user.getUuid().toString(), user.getUsername());
    }

    @Override
    public UserResponse login() {
        var user = securityService.getCurrentUserFromAuthentication();
        return new UserResponse(user.getUuid().toString(), user.getUsername());
    }

    private void validateUserExistence(String username) {
        if(userRepository.findByUsername(username).isPresent()) throw new PlannerException(Exceptions.USER_EXISTS);
    }

    private void validateUsername(String username) {
        if(username == null || username.length() < 4) throw new PlannerException(Exceptions.INVALID_USERNAME);
    }

    private void validatePassword(String password) {
        if(password == null || password.length() < 8) throw new PlannerException(Exceptions.INVALID_PASSWORD);
    }
}
