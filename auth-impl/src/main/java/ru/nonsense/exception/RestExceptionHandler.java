package ru.nonsense.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nonsense.auth.domain.entity.exception.ExceptionDto;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ExceptionDto> handleAuthenticationException(AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionDto()
                .setMessage(ex.getMessage()));
    }
}
