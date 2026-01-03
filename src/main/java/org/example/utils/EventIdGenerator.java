package org.example.utils;

public class EventIdGenerator {
    private static Long idCurent = 0L;

    /**
     * Genereaza id-uri unice pentru clasa Event
     * */
    public static synchronized Long nextId() {
        return ++idCurent;
    }
}
