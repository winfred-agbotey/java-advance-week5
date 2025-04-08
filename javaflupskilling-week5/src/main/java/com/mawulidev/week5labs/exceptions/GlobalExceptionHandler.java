package com.mawulidev.week5labs.exceptions;

import com.mawulidev.week5labs.dtos.ResponseHandler;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatusCode status,
                                                                  @NotNull WebRequest request) {
        List<ErrorResponse> errorResponse = ex.getAllErrors()
                .stream()
                .map(violations ->
                        new ErrorResponse(LocalDateTime.now(), violations.getDefaultMessage(), request.getDescription(false))
                ).toList();

        return ResponseHandler.errorResponse(HttpStatus.BAD_REQUEST, errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handlerAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseHandler.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of(errorResponse));
    }

    @ExceptionHandler({UserException.class, BadCredentialsException.class})
    public final ResponseEntity<Object> handlerBadRequest(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseHandler.errorResponse(HttpStatus.BAD_REQUEST, List.of(errorResponse));
    }

    @ExceptionHandler({SignatureException.class, InsufficientAuthenticationException.class})
    public final ResponseEntity<Object> handlerSecurityExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseHandler.errorResponse(HttpStatus.UNAUTHORIZED, List.of(errorResponse));
    }
    @ExceptionHandler({AccessDeniedException.class})
    public final ResponseEntity<Object> handlerSecurityAccessExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseHandler.errorResponse(HttpStatus.FORBIDDEN, List.of(errorResponse));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundExceptions(Exception ex, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseHandler.notFoundResponse(HttpStatus.NOT_FOUND, errorResponse);
    }


}
