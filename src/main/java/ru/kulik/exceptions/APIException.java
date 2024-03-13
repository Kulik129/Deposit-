package ru.kulik.exceptions;

public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }

}
