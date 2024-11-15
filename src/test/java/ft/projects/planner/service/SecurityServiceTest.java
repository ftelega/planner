package ft.projects.planner.service;

import ft.projects.planner.model.User;
import ft.projects.planner.security.model.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    private final SecurityService securityService = new SecurityServiceImpl();

    @Test
    public void givenAuthenticationAndUserDetailsImpl_whenGettingUserFromAuthentication_thenCorrectUserReturned() {
        var user = new User();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new UserDetailsImpl(user), null, List.of()));
        var res = securityService.getCurrentUserFromAuthentication();
        assertEquals(user, res);
    }

    @Test
    public void givenNoAuthentication_whenGettingUserFromAuthentication_thenThrow() {
        assertThrows(IllegalStateException.class, securityService::getCurrentUserFromAuthentication);
    }

    @Test
    public void givenInvalidPrincipal_whenGettingUserFromAuthentication_thenThrow() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", null, List.of()));
        assertThrows(IllegalStateException.class, securityService::getCurrentUserFromAuthentication);
    }
}