package org.example.utils;

public class UserIdGenerator {
    private static Long idCurent = 0L;

    /**
     * Genereaza id-uri unice pentru clasa Users
     * */
    public static synchronized Long nextId() {
        return ++idCurent;
    }
}
