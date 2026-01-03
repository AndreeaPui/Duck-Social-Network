//package org.example.service;
//
//import org.example.domain.*;
//import org.example.domain.rataFamily.SwimmingDuck;
//import org.example.domain.rataFamily.SwimmingFlyingDuck;
//import org.example.exceptii.AlreadyInRepoException;
//import org.example.exceptii.ArgumentException;
//import org.example.exceptii.NotInListException;
//import org.example.exceptii.ValidatorException;
//import org.example.repository.PagedRepository;
//import org.example.repository.Repository;
//import org.example.repository.database.RepoDBUserEvent;
//import org.example.repository.dto.Page;
//import org.example.repository.dto.Pageable;
//import org.example.validatori.Validate;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//
//public class ServiceEvenimente {
//    private PagedRepository<Event> repoEvent;
//    private PagedRepository<User> repoUsers;
//    private RepoDBUserEvent repoDBUserEvent;
//    private Validate validator;
//
//    public ServiceEvenimente(PagedRepository<Event> repoEvent, PagedRepository<User> repoUsers,RepoDBUserEvent repoDBUserEvent, Validate validator) {
//        this.repoEvent = repoEvent;
//        this.repoUsers = repoUsers;
//        this.repoDBUserEvent = repoDBUserEvent;
//        this.validator = validator;
//    }
//
//    /**
//     * Functie ce filtreaza toate ratele inotatoare
//     * @return o lista cu toate ratele inotatoare
//     * */
//    private List<Rata> filtrareSwimmingDucks(){
//        List<Rata> swimmingDucks = new ArrayList<>();
//
//        for(User u: repoUsers.findAll()){
//            if(u instanceof SwimmingDuck)
//                swimmingDucks.add((SwimmingDuck) u);
//            if(u instanceof SwimmingFlyingDuck)
//                swimmingDucks.add((SwimmingFlyingDuck) u);
//        }
//
//        return  swimmingDucks;
//    }
//
//    /**
//     * Functia adauga un eveniment in lista
//     * @param nr - Nr de culoare
//     * @throws AlreadyInRepoException daca exista deja evenimentul in lista
//     *         ValidatorException daca id-ul este negativ
//     * */
//    public void addEvent(Integer nr){
//        Event eveniment = new RaceEvent(nr);
//
//        repoEvent.save(eveniment);
//    }
//
//    /**
//     * Functia elimina un card din lista
//     * @param id - id-ul cardului pe care dorim sa il stergem
//     * @throws ArgumentException daca id este null
//     *        NotInListException daca nu gasim cardul in lista
//     * */
//    public void removeEvent(Long id) {
//        var remain = repoEvent.delete(id);
//        if(remain.isEmpty())
//            throw new NotInListException("Eventul nu exista!");
//    }
//
//    /**
//     * Functia returneaza toate cardurile
//     * @return - lista de carduri
//     * */
//    public List<Event> getEvents(){
//        return (ArrayList)repoEvent.findAll();
//    }
//
//
//    /**
//     * Functia returneaza toate cardurile
//     * @return - lista de carduri paginata
//     * */
//    public Page<Event> findEventsOnPage(Pageable pageable){
//        return repoEvent.findAllOnPage(pageable);
//    }
//
//    /**
//     * Functia cauta un card dupa un id dat
//     * @param id - id-ul cardului cautat
//     * @throws ArgumentException daca id este null
//     *        NotInListException daca nu exista un card cu id-ul respectiv
//     * */
//    public Event getEvent(Long id){
//        if(repoEvent.findOne(id).isEmpty())
//            throw new NotInListException("Cardul nu exista!");
//        else {
//            return repoEvent.findOne(id).get();
//        }
//    }
//
//
//
//    ////////////////////////////////////// OPERATII PE EVENT ///////////////////////////////////////////////
//
//    /**
//     * @param idEvent - id-ul evenimentului
//     * @param idUser - id-ul utilizatorului
//     * @throws NotInListException daca evenimentul sau utilizatorul nu exista
//     *        AlreadyInRepoException daca utilizatorul este deja abonat la eveniment
//     *        ArgumentException pt cand utilizatorul nu este valid
//     * */
//    public void subscribeToEvent(Long idEvent, Long idUser){
//        Optional<Event> eveniment = repoEvent.findOne(idEvent);
//        Optional<User> user0  = repoUsers.findOne(idUser);
//
//        if(eveniment.isEmpty()){
//            throw new NotInListException("Evenimentul nu exista!");
//        }
//
//        if(user0.isEmpty()){
//            throw new NotInListException("Userul nu exista!");
//        }
//
//
//        Event ev =  eveniment.get();
//        User user = user0.get();
//
//        UserEvent u = new UserEvent(idUser,idEvent);
//        var remain = repoDBUserEvent.save(u);
//
//        if(remain == -1)
//            throw new AlreadyInRepoException("Deja exista aceasta pereche!");
//    }
//
//
//    /**
//     * @param idEvent - id-ul evenimentului
//     * @param idUser - id-ul utilizatorului
//     * @throws NotInListException daca evenimentul sau utilizatorul nu exista SAU utilizatorul nu este subscriber al evenimentului
//     *         ArgumentException pt cand utilizatorul nu e valid
//     * */
//    public void unsubscribeFromEvent(Long idEvent, Long idUser){
//        Optional<Event> eveniment = repoEvent.findOne(idEvent);
//        Optional<User> user0  = repoUsers.findOne(idUser);
//
//        if(eveniment.isEmpty()){
//            throw new NotInListException("Evenimentul nu exista!");
//        }
//
//        if(user0.isEmpty()){
//            throw new NotInListException("Userul nu exista!");
//        }
//
//        Event ev =  eveniment.get();
//        User user = user0.get();
//
//        //ev.removeObserver(user);
//        //user.removeEvent(ev);
//
//        var remain = repoDBUserEvent.delete(idEvent,idUser);
//        if(remain == -1)
//            throw new NotInListException("Nu exista aceasta pereche!");
//    }
//
//
//    /**
//     * @param idEvent - id-ul evenimentului al caror subscriberi vor fi atentionati
//     * */
//    public void notifySubscribers(Long idEvent){
//        Optional<Event> ev =  repoEvent.findOne(idEvent);
//        if(ev.isEmpty())
//            throw new NotInListException("Evenimentul nu exista!");
//
//        ev.get().notifyObserver();
//    }
//
//
//    /**
//     * Functia afiseaza multimea de rate care dau cea mai rapida cursa
//     * @param idEvent - id-ul evenimentului
//     * @throws ArgumentException daca nr de candidati este mai mic decat nr de balize
//     * */
//    public List<Rata> cursa(Long idEvent){
//        List<Rata> candidati = filtrareSwimmingDucks();
//        Optional<Event> e = repoEvent.findOne(idEvent);
//
//        if(e.isEmpty())
//            throw new  NotInListException("Evenimentul nu exista!");
//
//        RaceEvent r = (RaceEvent) e.get();
//
//        if(r.getNrBalize() > candidati.size())
//            throw new ArgumentException("Nr de candidati este prea mic!");
//
//        List<Rata> topN = candidati.stream()
//                .sorted(Comparator.comparing(Rata::getRezistenta))
//                .limit(r.getNrBalize())
//                .toList();
//
//        return topN;
//    }
//
//}
