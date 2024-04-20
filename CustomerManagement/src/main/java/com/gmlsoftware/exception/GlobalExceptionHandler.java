package com.gmlsoftware.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.ws.rs.NotFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(getDefault(ex.getMessage()));
	}

	@ExceptionHandler({ PropertyReferenceException.class, NullPointerException.class, IllegalArgumentException.class, RuntimeException.class })
	public ResponseEntity<ExceptionResponse> handlePropertyReferenceException(RuntimeException re) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(getDefault(re.getMessage()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException dive) {
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(getDefault(dive.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
	    return ResponseEntity
	    		.status(HttpStatus.INTERNAL_SERVER_ERROR)
	    		.body(getDefault(ex.getMessage()));
	}

	private ExceptionResponse getDefault(String message) {
		return ExceptionResponse
				.builder()
				.message(message)
				.build();
	}
}
