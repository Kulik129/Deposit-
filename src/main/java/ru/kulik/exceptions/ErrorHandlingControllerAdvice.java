package ru.kulik.exceptions;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.kulik.validation.ValidationErrorResponse;
import ru.kulik.validation.Violation;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException ex) {
        final List<Violation> violations = ex.getConstraintViolations().stream()
                .map(it -> new Violation(it.getPropertyPath().toString(), it.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }


    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(it -> new Violation(it.getField(), it.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseBody
    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onValidationException(ConstraintViolationException ex) {
        final List<Violation> violations = ex.getConstraintViolations().stream()
                .map(it -> new Violation(it.getPropertyPath().toString(), it.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }



    @ResponseBody
    @ExceptionHandler(APIException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIExceptionsResponse apiExceptions(APIException ex) {
        return APIExceptionsResponse.builder().message(ex.getMessage()).build();
    }

//    @ResponseBody
//    @ExceptionHandler(APIException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ValidationErrorResponse ex(ConstraintViolationException ex) {
//        final List<Violation> violations = ex.getConstraintViolations().stream()
//                .map(it -> new Violation(it.getPropertyPath().toString(), it.getMessage()))
//                .collect(Collectors.toList());
//        return new ValidationErrorResponse(violations);
//    }
}
