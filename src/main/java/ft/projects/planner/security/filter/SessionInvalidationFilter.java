package ft.projects.planner.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SessionInvalidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            if((response.getStatus() + "").startsWith("2")) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
        }
    }
}
