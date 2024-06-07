package com.shadoww.BookLibraryApp.exception;

import com.shadoww.BookLibraryApp.dto.response.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {

//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "FORBIDDEN");
//        response.put("message", e.getMessage());

//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {

//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "Error");
//        response.put("message", e.getMessage());


//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {

        System.out.println(e.getMessage());

//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "NO_FOUND");
//        response.put("message", e.getMessage());


//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    public ResponseEntity<?> handleEntityNotFoundException(NullEntityReferenceException e, WebRequest request) {

//        System.out.println(e.getMessage());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", "BAD_REQUEST");
//        response.put("message", e.getMessage());


//        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException e, WebRequest request) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "NO_AUTHORIZED");
//        response.put("error", e.getMessage());

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        return buildErrorResponse(e,HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest request) {

//        System.out.println("error with finding user");
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "NO_FOUND");
//        response.put("error", e.getMessage());
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {

        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleInternalServerError(Exception e, WebRequest request) {

//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "INTERNAL_SERVER_ERROR");
//        response.put("error", e.getMessage());

//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, status);
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, String message, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, status);
    }

}
