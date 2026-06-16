package com.creativpressing.api.exception;
import com.creativpressing.api.dto.response.ApiErrorResponse; import jakarta.servlet.http.HttpServletRequest; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.LocalDateTime; import java.util.*;
@RestControllerAdvice public class GlobalExceptionHandler {
 @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<ApiErrorResponse> notFound(ResourceNotFoundException ex,HttpServletRequest req){return build(HttpStatus.NOT_FOUND,ex.getMessage(),req,null);} 
 @ExceptionHandler(BusinessException.class) public ResponseEntity<ApiErrorResponse> business(BusinessException ex,HttpServletRequest req){return build(HttpStatus.BAD_REQUEST,ex.getMessage(),req,null);} 
 @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<ApiErrorResponse> illegal(IllegalArgumentException ex,HttpServletRequest req){return build(HttpStatus.BAD_REQUEST,ex.getMessage(),req,null);} 
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ApiErrorResponse> validation(MethodArgumentNotValidException ex,HttpServletRequest req){ Map<String,String> errors=new LinkedHashMap<>(); ex.getBindingResult().getFieldErrors().forEach(e->errors.put(e.getField(),e.getDefaultMessage())); return build(HttpStatus.BAD_REQUEST,"Données invalides",req,errors);} 
 @ExceptionHandler(Exception.class) public ResponseEntity<ApiErrorResponse> generic(Exception ex,HttpServletRequest req){return build(HttpStatus.INTERNAL_SERVER_ERROR,"Erreur interne du serveur",req,null);} 
 private ResponseEntity<ApiErrorResponse> build(HttpStatus status,String msg,HttpServletRequest req,Map<String,String> validation){ return ResponseEntity.status(status).body(new ApiErrorResponse(LocalDateTime.now(),status.value(),status.getReasonPhrase(),msg,req.getRequestURI(),validation)); }
}
