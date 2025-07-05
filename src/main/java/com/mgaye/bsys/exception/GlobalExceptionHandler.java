package com.mgaye.bsys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ... existing handlers ...

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handleRoleNotFoundException(RoleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}