//package org.example.domain;
//import org.example.exceptii.ArgumentException;
//import org.example.exceptii.NotInListException;
//import org.example.observers.Observable;
//import org.example.observers.Observer;
//import org.example.utils.UserIdGenerator;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Event implements Observable{
//    //private Persoana manager;
//    private Long id;
//    private List<Observer> subscribers = new ArrayList<>();
//
//    public Event() {
//        this.id = UserIdGenerator.nextId();
//    }
//
//    public List<Observer> getUsers() {
//        return subscribers;
//    }
//
//    public List<Observer> getSubscribers() {
//        return subscribers;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @Override
//    public void addObserver(Observer o) {
//        if(o == null)
//            throw new ArgumentException("Rata is null");
//
//        User user = (User)o;
//
//        for(Observer e : this.subscribers) {
//            User u =  (User)e;
//            if (u.getId().equals(user.getId())) {
//                throw new ArgumentException("Acest eveniment deja se afla in lista userului!");
//            }
//        }
//
//        subscribers.add(o);
//    }
//
//    @Override
//    public void removeObserver(Observer o) {
//        if(o == null)
//            throw new ArgumentException("Rata is null");
//
//        User user = (User)o;
//
//        boolean removed = subscribers.removeIf(obs -> {
//            User u = (User) obs;
//            return u.getId().equals(user.getId());
//        });
//
//        if(!removed)
//            throw new NotInListException("Observer is not in list");
//    }
//
////    @Override
////    public void notifyObserver() {
////        for(Observer o : subscribers)
////            o.update(this);
////    }
//
//    @Override
//    public String toString() {
//        return "Event{" +
//                "id=" + id +
//                ", subscribers=" + subscribers +
//                '}';
//    }
//}
