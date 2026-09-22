package com.example.demo.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.ErrorResponse;

@ControllerAdvice 
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        ErrorResponse response = new ErrorResponse(400, errors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public  ResponseEntity<ErrorResponse> handleStudentNotFound(
        StudentNotFoundException ex
    ){
        ErrorResponse response = new ErrorResponse(404, List.of(ex.getMessage()));
        return  ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(DuplicateStudentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateStudent(
            DuplicateStudentException ex) {

        ErrorResponse response = new ErrorResponse(
                409,
                List.of(ex.getMessage())
        );

        return ResponseEntity
                .status(409)
                .body(response);
    }

}
