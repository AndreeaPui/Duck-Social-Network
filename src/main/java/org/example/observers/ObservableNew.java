package org.example.observers;

import org.example.utils.events.Event;

public interface ObservableNew <E extends Event>{
    void addObserver(ObserverNew<E> e);
    void removeObserver(ObserverNew<E> e);
    void notifyObservers(E t);
}
