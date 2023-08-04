package uol.compass.cspcapi.infrastructure.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    public static final String HASED_STRING = "$2a$12$4tCUci.7aboq13VUYTcp1ugg6km2OBZdtZ4AHjJKHI6SU94aYlGrW";
    private static JwtTokenProvider jwtTokenProvider;
    private static Authentication authentication;

    @BeforeAll
    static void setUp(){
        jwtTokenProvider = mock(JwtTokenProvider.class);
        authentication = mock(Authentication.class);
    }

    @Test
    void createToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "test@mail.com",
                "12345678"
        );

        when(jwtTokenProvider.createToken(any(Authentication.class))).thenReturn(HASED_STRING);

        String token = jwtTokenProvider.createToken(auth);

        assertEquals(HASED_STRING, token);
    }

    @Test
    void resolveToken() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authentication", "Bearer " + HASED_STRING);

        when(jwtTokenProvider.resolveToken(any(HttpServletRequest.class))).thenReturn(HASED_STRING);

        String response = jwtTokenProvider.resolveToken(request);

        verify(jwtTokenProvider).resolveToken(any(HttpServletRequest.class));
        assertNotNull(response);
        assertEquals(HASED_STRING, response);
    }

    @Test
    void validateToken() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);

        Boolean response = jwtTokenProvider.validateToken(HASED_STRING);

        assertTrue(response);
    }

    @Test
    void getUsername() {
        String t = "mail@test.com";
        when(jwtTokenProvider.getUsername(anyString())).thenReturn(t);

        String response = jwtTokenProvider.getUsername(HASED_STRING);

        assertEquals(t, response);
    }
}