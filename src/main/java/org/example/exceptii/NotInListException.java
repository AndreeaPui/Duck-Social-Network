package org.example.exceptii;

public class NotInListException extends RuntimeException {
    public NotInListException(String message) {
        super(message);
    }
}
