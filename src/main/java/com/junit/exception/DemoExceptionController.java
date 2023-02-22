package com.junit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DemoExceptionController {
	@ExceptionHandler(value = DemoItemNotFoundException.class)
	public ResponseEntity<Object> exception(DemoItemNotFoundException exception) {
		return new ResponseEntity<>("Demo not found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = DeleteDemoException.class)
	public ResponseEntity<Object> DeleteException(DeleteDemoException exception) {
		return new ResponseEntity<>("Exception with delete", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = DemoNameNullException.class)
	public ResponseEntity<Object> DeleteException(DemoNameNullException exception) {
		return new ResponseEntity<>("Name cannot be null", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
