package org.example.exceptii;

public class AlreadyInRepoException extends RuntimeException {
    public AlreadyInRepoException(String message) {
        super(message);
    }
}
