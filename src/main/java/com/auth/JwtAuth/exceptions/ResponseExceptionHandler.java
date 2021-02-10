package com.auth.JwtAuth.exceptions;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
/**
 * 
 * customizing the JSON error response :
 *
 */
@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(CustomException.class)
	protected ExceptionResponse handleExceptionsNotHandled(CustomException ex) {
		ExceptionResponse errorResponse = new ExceptionResponse.ExceptionResponseBuilder().withStatus(HttpStatus.BAD_REQUEST)
				.withDetail("something went wrong").withMessage(ex.getLocalizedMessage())
				.withError_code(HttpStatus.BAD_REQUEST.value()).atTime(LocalDateTime.now(ZoneOffset.UTC)).build();
		return errorResponse;
	}

	@Override
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorMsg = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getDefaultMessage())
				.collect(Collectors.toList());
		ExceptionResponse errorResponse = new ExceptionResponse.ExceptionResponseBuilder().withStatus(HttpStatus.NOT_ACCEPTABLE)
				.withDetail("not valid arguments").withMessage(errorMsg.toString())
				.withError_code(HttpStatus.NOT_ACCEPTABLE.value()).atTime(LocalDateTime.now(ZoneOffset.UTC)).build();
		return new ResponseEntity<>(errorResponse, status);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	protected ExceptionResponse handleAuthenticationException(AuthenticationException ex) {
		ExceptionResponse errorResponse = new ExceptionResponse.ExceptionResponseBuilder().withStatus(HttpStatus.BAD_REQUEST)
				.withDetail("authentication failed").withMessage(ex.getLocalizedMessage())
				.withError_code(HttpStatus.BAD_REQUEST.value()).atTime(LocalDateTime.now(ZoneOffset.UTC)).build();
		return errorResponse;
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse errorResponse = new ExceptionResponse.ExceptionResponseBuilder().withStatus(HttpStatus.BAD_REQUEST)
				.withDetail("Malformed JSON request - "+ex.getMessage()).withMessage(ex.getLocalizedMessage())
				.withError_code(HttpStatus.BAD_REQUEST.value()).atTime(LocalDateTime.now(ZoneOffset.UTC)).build();
		return new ResponseEntity<>(errorResponse, status);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UsernameNotFoundException.class)
	protected ExceptionResponse handleUserNotFoundException(Exception ex) {
		ExceptionResponse errorResponse = new ExceptionResponse.ExceptionResponseBuilder().withStatus(HttpStatus.NOT_FOUND)
				.withDetail("user not found").withMessage(ex.getLocalizedMessage())
				.withError_code(HttpStatus.NOT_FOUND.value()).atTime(LocalDateTime.now(ZoneOffset.UTC)).build();
		return errorResponse;
	}
}
