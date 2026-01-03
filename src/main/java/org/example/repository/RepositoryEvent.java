package org.example.repository;

import org.example.domain.Event;
import org.example.exceptii.AlreadyInRepoException;
import org.example.exceptii.ArgumentException;
import org.example.exceptii.NotInListException;

import java.util.List;
import java.util.Objects;
/*

public class RepositoryEvent<T extends Event> implements Repository<T> {
    List<T> evenimente;

    public RepositoryEvent(List<T> evenimente) {
        this.evenimente = evenimente;
    }

    @Override
    public void save(T ev) {
        if(ev==null)
            throw new IllegalArgumentException("Event is null");
        for(T event : evenimente){
            if(Objects.equals(event.getId(), ev.getId()))
                throw new AlreadyInRepoException("User is already in repository");
        }

        evenimente.add(ev);
    }

    @Override
    public void update(T entity) {

    }

    @Override
    public void delete(Long id) {
        if(id==null)
            throw new ArgumentException("Id is null");

        boolean status = false;
        for(T ev : evenimente){
            if(Objects.equals(ev.getId(), id)){
                evenimente.remove(ev);
                status = true;
                break;
            }
        }

        if(!status)
            throw new NotInListException("Event is not in repository");
    }

    @Override
    public List<T> findAll() {
        return evenimente;
    }

    @Override
    public T findOne(Long id) {
        if(id==null)
            throw new ArgumentException("Id is null");

        for(T ev : evenimente)
            if(Objects.equals(ev.getId(), id)) {
                return ev;
            }

        throw  new NotInListException("Event is not in repository");
    }
}
*/