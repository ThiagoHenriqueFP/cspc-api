package uol.compass.cspcapi.infrastructure.config.errorHandling;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseDTO;

import javax.naming.AuthenticationException;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handlerRuntime(
            RuntimeException ex
    ){

        ResponseDTO<String> responseDTO = new ResponseDTO<>(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handlerAuthentication(
            AuthenticationException ex
    ){
        ResponseDTO<String> responseDTO = new ResponseDTO<>(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ResponseDTO<List<String>> responseDTO = new ResponseDTO<>(errors);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handlerResponseStatus(
            ResponseStatusException ex
    ){
        ResponseDTO<?> responseDTO = new ResponseDTO<>(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }
}
