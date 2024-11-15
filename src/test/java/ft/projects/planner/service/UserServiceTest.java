package ft.projects.planner.service;

import ft.projects.planner.exception.PlannerException;
import ft.projects.planner.model.User;
import ft.projects.planner.model.UserRequest;
import ft.projects.planner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static ft.projects.planner.Constants.TEST_USERNAME;
import static ft.projects.planner.Constants.TEST_PASSWORD;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final SecurityService securityService = mock(SecurityService.class);
    private final UserService userService = new UserServiceImpl(userRepository, passwordEncoder, securityService);

    @Test
    public void givenValidUserRequest_whenRegister_thenVerifyCalls() {
        var user = User.builder()
                .Uuid(UUID.randomUUID())
                .build();
        given(userRepository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(user);
        var res = userService.register(new UserRequest(TEST_USERNAME, TEST_PASSWORD));
        verify(passwordEncoder, times(1)).encode(TEST_PASSWORD);
        verify(userRepository, times(1)).save(any());
        assertEquals(user.getUuid(), res);
    }

    @Test
    public void givenUserExists_whenRegister_thenThrow() {
        given(userRepository.findByUsername(TEST_USERNAME)).willReturn(Optional.of(new User()));
        assertThrows(PlannerException.class, () -> {
           userService.register(new UserRequest(TEST_USERNAME, TEST_PASSWORD));
        });
    }

    @Test
    public void givenNullUsername_whenRegister_thenThrow() {
        given(userRepository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        assertThrows(PlannerException.class, () -> {
            userService.register(new UserRequest(null, TEST_PASSWORD));
        });
    }

    @Test
    public void givenEmptyUsername_whenRegister_thenThrow() {
        given(userRepository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        assertThrows(PlannerException.class, () -> {
            userService.register(new UserRequest("", TEST_PASSWORD));
        });
    }

    @Test
    public void givenNullPassword_whenRegister_thenThrow() {
        given(userRepository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        assertThrows(PlannerException.class, () -> {
            userService.register(new UserRequest(TEST_USERNAME, null));
        });
    }

    @Test
    public void givenEmptyPassword_whenRegister_thenThrow() {
        given(userRepository.findByUsername(TEST_USERNAME)).willReturn(Optional.empty());
        assertThrows(PlannerException.class, () -> {
            userService.register(new UserRequest(TEST_USERNAME, ""));
        });
    }

    @Test
    public void whenLogin_thenVerifyCalls() {
        var user = User.builder()
                .username(TEST_USERNAME)
                .build();
        given(securityService.getCurrentUserFromAuthentication()).willReturn(user);
        var res = userService.login();
        verify(securityService, times(1)).getCurrentUserFromAuthentication();
        assertNotNull(res);
        assertEquals(user.getUsername(), res.username());
    }
}