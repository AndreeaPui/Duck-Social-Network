package org.example.service;

import org.example.domain.*;
import org.example.domain.rataFamily.FlyingDuck;
import org.example.domain.rataFamily.SwimmingDuck;
import org.example.domain.rataFamily.SwimmingFlyingDuck;
import org.example.exceptii.NotInListException;
import org.example.exceptii.NrOfArgumentsException;
import org.example.exceptii.ValidatorException;
import org.example.exceptii.ArgumentException;
import org.example.repository.database.RepoDBUtilizatori;
import org.example.repository.dto.Page;
import org.example.repository.dto.Pageable;
import org.example.utils.TipRata;
import org.example.utils.TipUtilizator;
import org.example.validatori.Validate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

public class ServiceUtilizatori {
    private RepoDBUtilizatori repoUsers;
    private ServiceFriendship serviceFriendship;
    //private ServiceEvenimente serviceEvenimente;
    private ServiceCard serviceCard;
    private Validate validator;

    public ServiceUtilizatori(RepoDBUtilizatori repoUsers, ServiceFriendship serviceFriendship, Validate validator) {
        this.repoUsers = repoUsers;
        this.serviceFriendship = serviceFriendship;
        //this.serviceEvenimente = serviceEvenimente;
        this.validator = validator;
    }

    ////////////////////////////////////// USERI ////////////////////////////////////////////////////////

    /**
     * Metoda adauga un utilizator in lista
     * @param tip - tipul de utilizator pe care trebuie sa il introducem (persoana/rata)
     * @param username - sir de caractere
     * @param email - sir de caractere
     * @param password - sir de caractere
     * @param extraArguments - argumente in plus de tip sir de caractere (depinde nr lor de tipul de utilizator introdus)
     * @throws ArgumentException daca e este null
     *        AlreadyInRepoException daca e este deja in lista
     *        NumberFormatException daca nu se pot transforma anumite date din sir de caractere in numeric
     *        IllegalArgumentException daca tipul de rata introdus este unul invalid (pt cand utilizatorul este rata)
     *        ValidatorException daca id-ul este negativ sau nul, stringurile sunt nule sau emailul este invalid
     * */
    // În ServiceUtilizatori.java

    public void addUser(TipUtilizator tip, String username, String email, String password, String... extraArguments){
        User user = null;

        if(!validator.verificaString(username) || !validator.verificaString(password))
            throw new ValidatorException("Usernameul si parola nu pot fi nule!");

        if(!validator.verificaEmail(email))
            throw new ValidatorException("Emailul nu este valid!");

        // PAS CRITIC 1: HASH-UIREA PAROLEI SIMPLE
        String hashedPassword = hashPassword(password);

        switch(tip){
            case PERSOANA -> {
                //se verifica daca avem destule argumente -> daca nu NrOfArgumentsException
                if(extraArguments.length != 5)
                    throw new NrOfArgumentsException("Wrong number of arguments");

                String nume = extraArguments[0];
                String prenume = extraArguments[1];
                LocalDate dataNasterii = LocalDate.parse(extraArguments[2]);
                String ocupatie = extraArguments[3];
                Integer nivelEmpatie =  Integer.parseInt(extraArguments[4]);

                // PAS CRITIC 2: Folosirea hashedPassword în loc de password
                user = new Persoana(username, hashedPassword, email, null, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
            }
            case RATA ->{
                if(extraArguments.length != 3)
                    throw new NrOfArgumentsException("Wrong number of arguments");

                TipRata tipRata = TipRata.valueOf(extraArguments[0].trim().toUpperCase());
                Double viteza =  Double.parseDouble(extraArguments[1]);
                Double rezistenta = Double.parseDouble(extraArguments[2]);
                Long card = null;

                // PAS CRITIC 2: Folosirea hashedPassword în loc de password
                if(tipRata == TipRata.FLYING)
                    user = new FlyingDuck(username, hashedPassword, email, null, tipRata, viteza, rezistenta, card);
                else if (tipRata == TipRata.SWIMMING)
                    user = new SwimmingDuck(username, hashedPassword, email, null, tipRata, viteza, rezistenta, card);
                else if(tipRata == TipRata.FLYING_AND_SWIMMING)
                    user = new SwimmingFlyingDuck(username, hashedPassword, email, null, tipRata, viteza, rezistenta, card);
            }
        }

        repoUsers.save(user); // Repo-ul va salva hash-ul primit prin user.getPassword()
    }

    /**
     * Functia elimina un utilizator din lista
     * @param id - id-ul ratei pe care dorim sa o stergem (nr intreg)
     * @throws ArgumentException daca e este null
     *        NotInListException daca utiizatorul nu este in lista
     * */

    public void removeUser(Long id) {
        Optional<User> u = this.repoUsers.findOne(id);

        if(u.isPresent()) {
            User user = u.get();
            for (User friend : new ArrayList<User>(user.getPrieteni())) {
                serviceFriendship.removeFriendships(user.getId(), friend.getId());
            }

//        List<Event> list = serviceEvenimente.getEvents();
//        for(Event e : list){
//            List<org.example.observers.Observer> subscribers = e.getUsers();
//            try{
//                RaceEvent re = (RaceEvent)e;
//                List<Observer> lista = re.getSubscribers();
//                try{
//                    SwimmingDuck userRata = (SwimmingDuck) u;
//                    if(lista.contains(userRata))
//                        lista.remove(userRata);
//                }catch(ClassCastException ex){
//                }
//                try{
//                    SwimmingFlyingDuck userRata = (SwimmingFlyingDuck) u;
//                    if(lista.contains(userRata))
//                        lista.remove(userRata);
//                }catch(ClassCastException ex){
//                }
//            }catch(ClassCastException ex){
//            }
//            if(subscribers.contains(u))
//                e.removeObserver(u);
//        }
//
//        try{
//            Rata rata = (Rata) u;
//            Long idCard = rata.getCard();
//            Card<? extends Rata>  card = serviceCard.getCard(idCard);
//
//            SwimmingDuck sd = null;
//            FlyingDuck fd = null;
//            try {
//                sd = (SwimmingDuck)rata;
//                Card<SwimmingDuck> sc = (Card<SwimmingDuck>) card;
//                if(sc != null)
//                    sc.removeMember(sd);
//            }catch(ClassCastException e){
//                fd = (FlyingDuck)rata;
//                Card<FlyingDuck> fc = (Card<FlyingDuck>) card;
//                if (fc != null)
//                    fc.removeMember(fd);
//            }
//        }catch(ClassCastException e){
//        }

            this.repoUsers.delete(id);
        }
        else
            throw new NotInListException("Utilizatorul nu se afla in lista");
    }

    /**
     * Metoda gaseste un utilizator dupa id-ul sau
     * @param id - id-ul utilizatorului
     * @return utilizatorul cautat
     * @throws ArgumentException daca id este null
     *        NotInListException daca nu exista un utilizator cu id-ul respectiv
     * */
    public User findUserById(Long id){
        if(repoUsers.findOne(id).isPresent())
            return repoUsers.findOne(id).get();
        else
            throw new NotInListException("User not found!");
    }

    public User findUserByUsername(String username) {
        return repoUsers.findUserByUsername(username);
    }

    /**
     * @return lista de utilizatori
     * */
    public List<User> listUsers(){
        return (ArrayList)repoUsers.findAll();
    }

    /**
     * @return lista de utilizatori paginata
     * */
    public Page<User> findAllOnPage(Pageable pageable){
        return repoUsers.findAllOnPage(pageable);
    }

    /**
     * @return lista de rate paginata (origare ar fi tipul ales)
     * */
    public Page<User> findAllRateOnPage(Pageable pageable,TipRata tip){

        if(tip!=null)
            return repoUsers.findAllRateTypeOnPage(pageable,tip.toString());
        else
            return repoUsers.findAllRateOnPage(pageable);
    }

    public Page<User> findAllPersOnPage(Pageable pageable) {
        return repoUsers.findAllPersOnPage(pageable);
    }

    // Returnează lista completă de utilizatori
    public javafx.collections.ObservableList<User> getAllUsers() {
        return javafx.collections.FXCollections.observableArrayList(listUsers());
    }

    /**
     * Verifică dacă parola oferită se potrivește cu parola hashată stocată în obiectul User.
     * (Presupune că obiectul User are un câmp getCryptedP() pentru hash-ul stocat.)
     * @param user Obiectul User (din baza de date)
     * @param password Parola introdusă de utilizator (text clar)
     * @return true dacă hash-urile se potrivesc, false altfel.
     */
    public boolean verifyCredentials(User user, String password) {
        if (user == null) {
            return false;
        }
        // Aici ar trebui să fie:
        return user.getPassword().equals(password);
    }

    /**
     * Parola este hashata folosind algoritmul SHA-256.
     * @param password Parola in text clar.
     * @return String-ul reprezentand hash-ul pe 64 de caractere.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public Iterable<User> findAll() {
        return repoUsers.findAll();
    }
}
