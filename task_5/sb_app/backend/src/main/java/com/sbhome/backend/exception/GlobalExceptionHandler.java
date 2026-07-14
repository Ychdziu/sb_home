package com.sbhome.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAge(BusinessValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

	@ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
	public ResponseEntity<Map<String, String>> handleBadRequest(Exception ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Invalid or missing request parameter."));
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred."));
    }
	
}