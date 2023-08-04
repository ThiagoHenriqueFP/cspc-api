package uol.compass.cspcapi.infrastructure.config.errorHandling;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseDTO;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultControllerAdviceTest {

    private static RuntimeException runtimeException;
    private static AuthenticationException authenticationException;
    private static ResponseStatusException responseStatusException;

    private static DefaultControllerAdvice defaultControllerAdvice;
//    private MethodArgumentNotValidException methodArgumentNotValidException;


    @BeforeAll
    static void setUp() {
        runtimeException = new RuntimeException("exception");
        authenticationException = new AuthenticationException("user not authenticated");
        responseStatusException = new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "this user isn`t allowed"
        );
//        methodArgumentNotValidException = new MethodArgumentNotValidException();

        defaultControllerAdvice = mock(DefaultControllerAdvice.class);

    }

    @Test
    void handlerRuntime() {
        ResponseDTO<String> responseDTO = new ResponseDTO<>("exception");

        when(defaultControllerAdvice.handlerRuntime(any(RuntimeException.class))).thenReturn(
                new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST)
        );

        ResponseEntity<Object> response = defaultControllerAdvice.handlerRuntime(runtimeException);

        verify(defaultControllerAdvice).handlerRuntime(runtimeException);
        ResponseDTO<String> parse = (ResponseDTO<String>) response.getBody();
        assertNotNull(parse);
        assertEquals(responseDTO.getData(), parse.getData());
        assertInstanceOf(ResponseEntity.class, response);
    }

    @Test
    void handlerAuthentication() {
        ResponseDTO<String> responseDTO = new ResponseDTO<>("user not authenticated");

        when(defaultControllerAdvice.handlerAuthentication(any(AuthenticationException.class))).thenReturn(
                new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST)
        );

        ResponseEntity<Object> response = defaultControllerAdvice.handlerAuthentication(authenticationException);

        verify(defaultControllerAdvice).handlerAuthentication(authenticationException);
        ResponseDTO<String> parse = (ResponseDTO<String>) response.getBody();
        assertNotNull(parse);
        assertEquals(responseDTO.getData(), parse.getData());
        assertInstanceOf(ResponseEntity.class, response);
    }

//    @Test
//    void handleMethodArgumentNotValid() {
//    }

    @Test
    void handlerResponseStatus() {
        ResponseDTO<String> responseDTO = new ResponseDTO<>("response status exception");

        when(defaultControllerAdvice.handlerResponseStatus(any(ResponseStatusException.class))).thenReturn(
                new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST)
        );

        ResponseEntity<Object> response = defaultControllerAdvice.handlerResponseStatus(responseStatusException);

        verify(defaultControllerAdvice).handlerResponseStatus(responseStatusException);
        ResponseDTO<String> parse = (ResponseDTO<String>) response.getBody();
        assertNotNull(parse);
        assertEquals(responseDTO.getData(), parse.getData());
        assertInstanceOf(ResponseEntity.class, response);
    }
}