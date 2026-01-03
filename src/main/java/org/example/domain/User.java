package org.example.domain;
import org.example.exceptii.ArgumentException;
import org.example.exceptii.NotInListException;
import org.example.observers.Observer;
import org.example.utils.FriendshipIdGenerator;
import org.example.utils.UserIdGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private Long id;
    private String username;
    private String password;
    private String email;

    private List<User> prieteni = new ArrayList<User>();

    public User(String username, String password, String email, List<User> prieteni) {
        this.id = UserIdGenerator.nextId();
        this.username = username;
        this.password = password;
        this.email = email;
        this.prieteni = prieteni;
    }

//    //throws ArgumentException pt cand evenimentul este deja in lista de evenimente
//    public void addEvent(Event event)
//    {
//        for(Event e : this.evenimente)
//            if(e.getId().equals(event.getId())) {
//                throw new ArgumentException("Acest eveniment deja se afla in lista userului!");
//            }
//
//        evenimente.add(event);
//    }
//
//    public void removeEvent(Event event) {
//        boolean removed = evenimente.removeIf(ev -> {
//            return ev.getId().equals(event.getId());
//        });
//
//        if(!removed)
//            throw new NotInListException("Evenimentul nu se afla in lista userului!");
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getPrieteni() {
        return prieteni;
    }

    //metodele generale

    //metoda de logare a unui utilizator
    abstract void login();

    //metoda de delogare a unui utilizator
    abstract void logout();

    //metoda de trimitere a unui mesaj catre alt utilizator
//    abstract void sendMessage(Message message);

    //metoda de receptionare a unui mesaj de la alt utilizator
//    abstract void receiveMessage(Message message);

    @Override
    public String toString() {
        List<String> prietenii = new ArrayList<>();
        for(User u : prieteni)
            prietenii.add(u.getId().toString());

//        List<Long> eventIds = evenimente
//                .stream()
//                .map(Event::getId)
//                .toList();

        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", prieteni=" + prietenii +
              //  ", evenimente=" + (!evenimente.isEmpty() ? eventIds : null) +
                '}';
    }
}

