package ru.kulik.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

//@ControllerAdvice
//@RestController
//public class fdvdf {
//    @ExceptionHandler
//    public ResponseEntity<APIExceptionsResponse> notFound(APIException ex) {
//        return new ResponseEntity<>(APIExceptionsResponse.builder().message(ex.getMessage()).build(), HttpStatus.NOT_FOUND);
//    }
//}
