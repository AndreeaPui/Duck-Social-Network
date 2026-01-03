package org.example.repository;

import org.example.domain.User;
import org.example.exceptii.AlreadyInRepoException;
import org.example.exceptii.ArgumentException;
import org.example.exceptii.NotInListException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/*

public class RepositoryClasic<E extends User> implements Repository<E> {
    private List<E> utilizatori = new ArrayList<>();

    public RepositoryClasic(List<E> utilizatori) {
        this.utilizatori = utilizatori;
    }

    @Override
    public void save(E e) {
        if(e==null)
            throw new IllegalArgumentException("User is null");
        for(E u : utilizatori){
            if(Objects.equals(u.getId(), e.getId()))
                throw new AlreadyInRepoException("User is already in repository");
        }

        utilizatori.add(e);
    }

    @Override
    public void update(E entity) {

    }

    @Override
    public void delete(Long id) {
        if(id==null)
            throw new ArgumentException("Id is null");

        boolean status = false;
        for(E u : utilizatori){
            if(Objects.equals(u.getId(), id)){
                utilizatori.remove(u);
                status = true;
                break;
            }
        }

        if(!status)
            throw new NotInListException("User is not in repository");
    }

    @Override
    public List<E> findAll() {
        return utilizatori;
    }

    @Override
    public E findOne(Long id) {
        if(id==null)
            throw new ArgumentException("Id is null");

        for(E u : utilizatori)
            if(Objects.equals(u.getId(), id)) {
                return u;
            }

        throw  new NotInListException("User is not in repository");
    }
}
*/
