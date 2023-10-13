package br.com.devmaicon.todolist.errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        var errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        var errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errorResponse = new HashMap<>();
        errorResponse.put("error", "Erro de validação");
        
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        
        errorResponse.put("details", errorMessages);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
