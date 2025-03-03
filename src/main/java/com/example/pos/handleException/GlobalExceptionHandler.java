package com.example.pos.handleException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.pos.dto.ResponseData;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        ResponseData<Object> response = new ResponseData<>(HttpStatus.FORBIDDEN.value(),
                "You do not have permission to access.", null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseData<Object>> handleJwtException(JwtException ex) {
        ResponseData<Object> response = new ResponseData<>(HttpStatus.UNAUTHORIZED.value(),
                "Token expired or invalid!", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<Object>> handleGeneralException(Exception ex) {
        ResponseData<Object> response = new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "System error: " + ex.getMessage(),
                null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
