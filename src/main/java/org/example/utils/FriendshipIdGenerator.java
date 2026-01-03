package org.example.utils;

public class FriendshipIdGenerator {
    private static Long idCurent = 0L;

    /**
     * Genereaza id-uri unice pentru clasa Friendships
     * */
    public static synchronized Long nextId() {
        return ++idCurent;
    }
}
