package ft.projects.planner.service;

import ft.projects.planner.model.User;
import ft.projects.planner.security.model.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public User getCurrentUserFromAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof UserDetailsImpl userDetails) return userDetails.getUser();
        throw new IllegalStateException("User not present");
    }
}
