package org.example.service;

import org.example.domain.Card;
import org.example.domain.Rata;
import org.example.domain.User;
import org.example.domain.cardFamily.CardInotatoare;
import org.example.domain.cardFamily.CardZburatoare;
import org.example.domain.rataFamily.FlyingDuck;
import org.example.domain.rataFamily.SwimmingDuck;
import org.example.domain.rataFamily.SwimmingFlyingDuck;
import org.example.exceptii.ArgumentException;
import org.example.exceptii.NotInListException;
import org.example.exceptii.ValidatorException;
import org.example.repository.PagedRepository;
import org.example.repository.Repository;
import org.example.repository.dto.Page;
import org.example.repository.dto.Pageable;
import org.example.utils.TipRata;
import org.example.validatori.Validate;

import java.util.*;

public class ServiceCard {
    private PagedRepository<Card<?extends Rata>> repoCard;
    private PagedRepository<User> repoUsers;
    private Validate validator;

    public ServiceCard(PagedRepository<Card<? extends Rata>> repoCard, PagedRepository<User> repoUsers,Validate validator) {
        this.repoCard = repoCard;
        this.repoUsers = repoUsers;
        this.validator = validator;
    }

    /**
     * Functia adauga un card in lista
     * @param tip - un sir de caractere ce indica tipul cardului pe care dorim sa il adaugam
     * @param numeCard - sir de caractere ce indica numele cardului
     * @throws ArgumentException daca tipul de card dat nu este valabil
     *        AlreadyInRepoException daca exista deja cardul in lista
     *        ValidatorException daca daca stringurile sunt nule sau id ul este negativ
     * */
    public void addCard(String tip,String numeCard){
        Card<? extends Rata> card;

        if(!validator.verificaString(tip) | !validator.verificaString(numeCard))
            throw new ValidatorException("Tipul si cardul trebuie sa fie nenul!");

        if(tip.equalsIgnoreCase("SWIMMING")){
            card = new CardInotatoare(numeCard);
        }
        else if (tip.equalsIgnoreCase("FLYING"))
            card = new CardZburatoare(numeCard);
        else
            throw new ArgumentException("Tip invalid");

        repoCard.save(card);
    }

    /**
     * Functia elimina un card din lista
     * @param id - id-ul cardului pe care dorim sa il stergem
     * @throws ArgumentException daca id este null
     *        NotInListException daca nu gasim cardul in lista
     * */
    public void removeCard(Long id) {
        var remain = repoCard.delete(id);
        if(remain.isEmpty())
            throw new NotInListException("Cardul nu exista!");

        for(User u : repoUsers.findAll()){
            if(u instanceof Rata){
                Rata r = (Rata) u;
                if(r.getCard().equals(id)) {
                    r.setCard(null);
                    repoUsers.update(r);
                }
            }
        }
    }

    /**
     * Functia returneaza toate cardurile
     * @return - lista de carduri
     * */
    public List<Card<? extends Rata>> getCarduri(){
        return (ArrayList)repoCard.findAll();
    }

    /**
     * Functia returneaza toate cardurile
     * @return - lista de carduri paginata
     * */
    public Page<Card<? extends Rata>> findCarduriOnPage(Pageable pageable){
        return repoCard.findAllOnPage(pageable);
    }


    /**
     * Functia cauta un card dupa un id dat
     * @param id - id-ul cardului cautat
     * @throws ArgumentException daca id este null
     *        NotInListException daca nu exista un card cu id-ul respectiv
     * */
    public Card<? extends Rata> getCard(Long id){
        if(repoCard.findOne(id).isPresent())
            return repoCard.findOne(id).get();
        else
            throw new NotInListException("Cardul cautat nu exista!");
    }




    /////////////////////////////////////// OPERATII PE CARD //////////////////////////////////////////////////////

    /**
     * Functia adauga o rata la un card
     * @param idCard - id-ul cardului pe care dorim sa il modificam
     * @param idRata - id-ul ratei pe care dorim sa o adaugam
     * @throws ArgumentException daca tipul ratei nu se potriveste cu cel al cardului
     *        AlreadyInRepoException daca rata se afla deja in card
     *        NotInListException daca cardul sau rata nu exista
     * */
    public void addRataCard(Long idCard, Long idRata){
        Optional<Card<? extends Rata>> card = repoCard.findOne(idCard);
        Optional<User> u =  repoUsers.findOne(idRata);

        if(card.isEmpty() || u.isEmpty()){
            throw new NotInListException("Cardul sau rata cautata nu exista!");
        }


        Card<? extends Rata> cardCautat =  card.get();
        User rataCautata =  u.get();

        if (!(rataCautata instanceof Rata))
            throw new ArgumentException("Userul trebuie sa fie o rata!");

        Rata rata =  (Rata) rataCautata;

        if((cardCautat instanceof CardInotatoare && rata.getTip()== TipRata.FLYING) || (cardCautat instanceof CardZburatoare && rata.getTip()==TipRata.SWIMMING))
            throw new ArgumentException("Tip invalid de rata pt card");

        Card<Rata> cardRata = (Card<Rata>) cardCautat;
        cardRata.addMember(rata);
        //rata.setCard(card.getId());
        repoUsers.update(rata);
    }

    /**
     * Functia elimina o rata dintr-un card
     * @param idCard - id-ul cardului pe care dorim sa il modificam
     * @param idRata - id-ul ratei pe care dorim sa o eliminam
     * @throws NotInListException daca cardul sau rata nu exista SAU daca rata nu se afla in card
     * */
    public void removeRataCard(Long idCard, Long idRata) {
        Optional<Card<? extends Rata>> card = repoCard.findOne(idCard);
        Optional<User> u =  repoUsers.findOne(idRata);

        if(card.isEmpty()){
            throw new NotInListException("Cardul cautat nu exista!");
        }

        if(u.isEmpty()){
            throw new NotInListException("Rata cautata nu exista!");
        }

        Rata rata = null;

        try {
            rata = (Rata) u.get();
        }catch (ClassCastException e){
            throw new ArgumentException("Userul nu este o rata!");
        }

        if(!rata.getCard().equals(idCard)){
            throw new NotInListException("Rata nu se afla in card!");
        }


        Card<? extends Rata> cardCautat =  card.get();

        Card<Rata> card0 = (Card<Rata>) cardCautat;
        card0.removeMember(rata);

        repoUsers.update(rata);
    }


    /**
     * Functia returneaza performanta - media rezistentelor si media vitezelor
     * @param idCard - id-ul cardului
     * @throws NotInListException daca nu exista cardul
     * */
    public AbstractMap.SimpleEntry<Double,Double> getPerformantaMedieService(Long idCard){
        Optional<Card<? extends Rata>> cardCautat =  repoCard.findOne(idCard);
        if(cardCautat.isEmpty())
            throw new  NotInListException("Cardul cautat nu exista!");

        Card<Rata> card = (Card<Rata>) cardCautat.get();
        List<Rata> membri = new ArrayList<>();

        for(User u: repoUsers.findAll())
            if(u instanceof SwimmingDuck || u instanceof FlyingDuck || u instanceof SwimmingFlyingDuck){
                Rata r = (Rata) u;
                if(Objects.equals(r.getCard(), idCard)) {
                    Optional<User> rata = repoUsers.findOne(idCard);
                    if(rata.isPresent()) {
                        membri.add((Rata) rata.get());
                    }
                }
            }

        Double rezistente = 0.0;
        Double viteze = 0.0;

        for(Rata m: membri){
            rezistente += m.getRezistenta();
            viteze += m.getViteza();
        }

        return new AbstractMap.SimpleEntry<>(rezistente/membri.size(),viteze/membri.size());


        //return card.getPerformantaMedie();
    }
}
