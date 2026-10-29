package com.example.aitickets.common;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e) {
    String msg=e.getBindingResult().getFieldErrors().stream().findFirst()
      .map(x->x.getField()+": "+x.getDefaultMessage()).orElse("Invalid request");
    return ResponseEntity.badRequest().body(Map.of("error",msg));
  }
  @ExceptionHandler(ResponseStatusException.class)
  ResponseEntity<Map<String,String>> status(ResponseStatusException e) {
    return ResponseEntity.status(e.getStatusCode()).body(Map.of("error",e.getReason()==null?"Request failed":e.getReason()));
  }
  @ExceptionHandler(IllegalStateException.class)
  ResponseEntity<Map<String,String>> ai(IllegalStateException e) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error",e.getMessage()));
  }
  @ExceptionHandler(Exception.class)
  ResponseEntity<Map<String,String>> unexpected(Exception e) {
    return ResponseEntity.status(500).body(Map.of("error","Unexpected server error. Check backend logs."));
  }
}
