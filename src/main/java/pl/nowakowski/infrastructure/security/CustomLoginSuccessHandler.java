package pl.nowakowski.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("MECHANIC")) {
                response.sendRedirect("/car-dealership/mechanic");
                return;
            } else if (role.equals("SALESMAN")) {
                response.sendRedirect("/car-dealership/salesman");
                return;
            } else if (role.equals("ADMIN")) {
                response.sendRedirect("/car-dealership");
                return;
            } else if (role.equals("REST_API")) {
                response.sendRedirect("/car-dealership/api");
                return;
            }
        }

        // Default redirect if no specific role matched
        response.sendRedirect("/car-dealership");
    }
}
