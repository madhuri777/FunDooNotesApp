package com.bridgeit.fundoonotes.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler{

	 @ExceptionHandler(value=LoginException.class)
	 public ResponseEntity<LoginException> getLoginException(LoginException exception){
		 return new ResponseEntity<LoginException>(exception,HttpStatus.NOT_FOUND);
	 }

     @ExceptionHandler(value=DataBaseException.class)
     public ResponseEntity<?> getDatabaseException(DataBaseException baseException){
    	 return new ResponseEntity<String>(baseException.getMessage(),HttpStatus.CONFLICT);
     }
}
