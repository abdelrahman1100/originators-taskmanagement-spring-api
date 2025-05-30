package com.masteryhub.todoapp.handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleBadCredentialsException(
      BadCredentialsException ex) {
    return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserNotFoundException(
      UsernameNotFoundException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, "User not found");
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<Map<String, String>> handleJwtException(JwtException ex) {
    return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request or argument");
  }

  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<Map<String, String>> handleNumberFormatException(NumberFormatException e) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Invalid number format");
    response.put("message", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  private ResponseEntity<Map<String, String>> buildErrorResponse(
      HttpStatus status, String message) {
    Map<String, String> response = new HashMap<>();
    response.put("error", status.getReasonPhrase());
    response.put("message", message);
    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    Map<String, String> response = new HashMap<>();
    Throwable cause = ex.getCause();
    if (cause instanceof InvalidFormatException invalidFormatException) {
      Class<?> targetType = invalidFormatException.getTargetType();
      if (targetType.isEnum()) {
        response.put("error", "Invalid enum value");
        response.put("message", "Allowed values: " + getEnumValues(targetType));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }
    }

    response.put("error", "Invalid request format");
    response.put("message", "Request body is not in the expected format");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  private String getEnumValues(Class<?> enumClass) {
    Object[] enumConstants = enumClass.getEnumConstants();
    return enumConstants != null
        ? String.join(", ", Arrays.stream(enumConstants).map(Object::toString).toList())
        : "Unknown enum";
  }
}
