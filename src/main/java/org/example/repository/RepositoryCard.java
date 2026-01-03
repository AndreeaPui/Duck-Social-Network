package org.example.repository;

import org.example.domain.Card;
import org.example.domain.Rata;
import org.example.exceptii.AlreadyInRepoException;
import org.example.exceptii.ArgumentException;
import org.example.exceptii.NotInListException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/*

public class RepositoryCard<T extends Rata> implements Repository<Card<T>> {

    private List<Card<T>> carduri = new ArrayList<>();

    public RepositoryCard(List<Card<T>> carduri) {
        this.carduri = carduri;
    }

    @Override
    public void save(Card<T> card) {
        if(card == null)
            throw new ArgumentException("Cardul nu poate fi null!");

        for(Card<? extends Rata> c: carduri)
            if(c.getId().equals(card.getId()))
                throw new AlreadyInRepoException("Cardul se afla deja in lista!");

        carduri.add(card);
    }

    @Override
    public void update(Card<T> entity) {

    }

    @Override
    public void delete(Long id) {
        if(id == null)
            throw new ArgumentException("Id nu poate fi null!");

        boolean status = false;
        for(Card<? extends Rata> c: carduri){
            if(Objects.equals(c.getId(), id)){
                carduri.remove(c);
                status = true;
                break;
            }
        }

        if(!status)
            throw new NotInListException("Cardul nu se afla in lista!");
    }

    @Override
    public List<Card<T>> findAll() {
        return carduri;
    }

    @Override
    public Card<T> findOne(Long id) {
        if(id==null)
            throw new ArgumentException("Id is null");

        for(Card<T> c : carduri)
            if(Objects.equals(c.getId(), id)) {
                return c;
            }

        throw  new NotInListException("Cardul cautat nu se afla in lista!");
    }
}
*/