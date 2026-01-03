package org.example.domain.validatori;

public interface Validation<T>{
    void validate(T entity) throws ValidationException;
    }

