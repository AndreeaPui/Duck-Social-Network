package org.example.domain;
import org.example.exceptii.AlreadyInRepoException;
import org.example.exceptii.NotInListException;
import org.example.utils.UserIdGenerator;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Card <T extends Rata> {
    private Long id;
    private String numeCard;
    private List<T> membri = new ArrayList<T>();

    public Card(String numeCard) {
        this.id = UserIdGenerator.nextId();
        this.numeCard = numeCard;
    }

    public Long getId() {
        return id;
    }

    public String getNumeCard() {
        return numeCard;
    }

    public List<T> getMembri() {
        return membri;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumeCard(String numeCard) {
        this.numeCard = numeCard;
    }

    /**
     * Adauga o rata/membru in card
     * @param member - rata care trebuie adaugata in card
     * @throws AlreadyInRepoException atunci cand rata se afla deja in card
     * */
    public void addMember(T member) {
        if(Objects.equals(member.getCard(), this.getId()))
            throw new AlreadyInRepoException("Rata se afla deja in card!");

        membri.add(member);
        member.setCard(this.getId());
    }

    /**
     * Elimina o rata din card
     * @param member - rata care trebuie eliminata din card
     * @throws NotInListException atunci cand rata nu se afla in card
     * */
    public void removeMember(T member) {
        if(!Objects.equals(member.getCard(), this.getId()))
            throw new NotInListException("Rata nu se afla in card!");

        membri.removeIf(m -> m.getId().equals(member.getId()));

        member.setCard(null);
    }

    /**
     * Calculeaza media rezistentei si a vitezei ratelor din card
     * @return o pereche de Double, repezentand: media rezistentelor si media vitezelo
     * */
    public SimpleEntry<Double,Double> getPerformantaMedie(){
        //media vitezelor si rezistentelor
        Double rezistente = 0.0;
        Double viteze = 0.0;

        for(int index = 0; index < membri.size(); index++){
            rezistente += membri.get(index).getRezistenta();
            viteze += membri.get(index).getViteza();
        }

        return new SimpleEntry<>(rezistente/membri.size(),viteze/membri.size());
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", numeCard='" + numeCard + '\'' +
                ", membri=" + membri +
                '}';
    }
}
