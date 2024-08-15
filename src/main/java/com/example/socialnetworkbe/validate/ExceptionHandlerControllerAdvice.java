package com.example.socialnetworkbe.validate;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    public static List<String> getMessageError(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            messages.add(error.getDefaultMessage());
        }
        return messages;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleNullPointerException(IllegalArgumentException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<String>> handleValidationException(ValidationException ex) {
        String message = ex.getLocalizedMessage();
        String[] errorsArray = message.split(";\\s*");
        List<String> result = Arrays.asList(errorsArray);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
