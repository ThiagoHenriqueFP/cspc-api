package uol.compass.cspcapi.infrastructure.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException {

        response.setStatus((HttpServletResponse.SC_UNAUTHORIZED));
        response.setContentType("application/json");
        response.getWriter().write( "\"message\": \"" + authException.getMessage() + "\"");
    }

//    @Override
//    public void afterPropertiesSet() {
//        setRealmName("JWT Authentication");
//        super.afterPropertiesSet();
//    }
}
