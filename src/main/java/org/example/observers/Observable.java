package org.example.observers;

import org.example.exceptii.ArgumentException;

public interface Observable {
    /**
     * adauga un observer in lista, daca acesta este valid
     * @throws ArgumentException pt cand nu este valid
     *        AlreadyInRepoException pt cand deja este abonat
     * */
    void addObserver(Observer o);

    /**
     * sterge un observer din lista atunci cand se poate
     * @throws ArgumentException pt cand nu e valid
     *        NotInListException pt cand observerul e care dorim sa il stergem nu este in lista
     * */
    void removeObserver(Observer o);

    /**
     * notifica observerii
     * */
    void notifyObserver();

}
