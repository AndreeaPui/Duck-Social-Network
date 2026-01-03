package org.example.observers;

import org.example.utils.events.Event;

public interface ObserverNew <E extends Event>{
    /**
     * va trimite mai departe messajul catre observatori => acestia afisand-ul pe ecran
     * */
    void update(E event);
}
