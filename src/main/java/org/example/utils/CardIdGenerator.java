package org.example.utils;

public class CardIdGenerator {
    private static Long idCurent = 0L;

    /**
     * Genereaza id-uri unice pentru clasa Card
     * */
    public static synchronized Long nextId() {
        return ++idCurent;
    }
}
