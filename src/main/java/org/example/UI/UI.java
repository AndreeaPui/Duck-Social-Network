//package org.example.UI;
//
//import org.example.exceptii.ArgumentException;
//import org.example.exceptii.NotInListException;
//import org.example.exceptii.AlreadyInRepoException;
//import org.example.exceptii.NrOfArgumentsException;
//import org.example.exceptii.ValidatorException;
//import org.example.service.Service;
//import org.example.utils.TipUtilizator;
//
//import java.time.format.DateTimeParseException;
//import java.util.*;
//
//public class UI {
//    Service service;
//
//    public UI(Service service) {
//        this.service = service;
//    }
//
//    //////////////////////////////////////////// USERS /////////////////////////////////////////
//
//
//    private void addUserUI(){
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Introduce username:");
//        String username = scanner.next();
//
//        System.out.println("Introduce email:");
//        String email = scanner.next();
//
//        System.out.println("Introduce password:");
//        String password = scanner.next();
//
//        System.out.println("Introduce tipul de utilizator:");
//        String tipul = scanner.next();
//
//        if(tipul.toLowerCase(Locale.ROOT).equals("persoana")){
//            System.out.println("Introduce numele:");
//            String numele = scanner.next();
//
//            System.out.println("Introduce prenumele:");
//            String prenumele = scanner.next();
//
//            System.out.println("Introduce data nasterii (yyyy-mm-dd):");
//            String dataNasterii = scanner.next();
//
//            System.out.println("Introduce ocupatia:");
//            String ocupatia = scanner.next();
//
//            System.out.println("Introduce nivelul de empatie (nr intreg):");
//            String nivel = scanner.next();
//
//            try{
//                service.getServiceUsers().addUser(TipUtilizator.PERSOANA,username,email,password,numele,prenumele,dataNasterii,ocupatia,nivel);
//                System.out.println("Operatie realizata cu succes");
//            }catch(NumberFormatException e){
//                System.out.println("Nivelul trebuie sa fie nr intreg");
//            }
//            catch(DateTimeParseException e){
//                System.out.println("Data trebuie sa aiba formatul: yyyy-mm-dd");
//            }
//            catch(ArgumentException | AlreadyInRepoException | NrOfArgumentsException | ValidatorException e){
//                System.out.println(e.getMessage());
//            }
//        }
//        else if(tipul.toLowerCase(Locale.ROOT).equals("rata")){
//            System.out.println("Introduce tipul de rata:");
//            String tipulRata = scanner.next().trim().toUpperCase();
//
//            System.out.println("Introduce viteza ratei (nr real):");
//            String viteza = scanner.next();
//
//            System.out.println("Introduce rezistenta (nr real):");
//            String rezistenta = scanner.next();
//
//            try{
//                service.getServiceUsers().addUser(TipUtilizator.RATA,username,email,password,tipulRata,viteza,rezistenta);
//                System.out.println("Operatie realizata cu succes");
//            }
//            catch(ArgumentException | AlreadyInRepoException | NrOfArgumentsException | ValidatorException e){
//                System.out.println(e.getMessage());
//            }
//            catch(NumberFormatException e){
//                System.out.println("Viteza si rezistenta trebuie sa fie nr reale");
//            }
//            catch(IllegalArgumentException e){
//                System.out.println("Tipul de rata nu este unul valid");
//            }
//        }
//        else{
//            System.out.println("Ai introdus un tip invalid de utilizator. Alege dintre: PERSOANA sau RATA");
//        }
//    }
//
//    private void removeUserUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul utilizatorului:");
//
//        Long id = null;
//        try{
//            id = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try{
//            service.getServiceUsers().removeUser(id);
//            System.out.println("Operatie realizata cu succes");
//        }catch(ArgumentException | NotInListException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//    //////////////////////////////////////////// FRIENDSHIPS ////////////////////////////////////////////////
//
//    private void addFriendshipUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Id-ul primului utilizator:");
//        Long id1 = null;
//        try{
//            id1 = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        System.out.println("Id-ul celui de-al doilea utilizator:");
//        Long id2 = null;
//        try{
//            id2 = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try{
//            service.getServiceFriendship().addFriendships(id1,id2);
//            System.out.println("Operatie realizata cu succes");
//        }catch(ArgumentException | AlreadyInRepoException | NotInListException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void removeFriendshipUI(){
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Id-ul primului utilizator:");
//        Long id1 = null;
//        try{
//            id1 = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        System.out.println("Id-ul celui de-al doilea utilizator:");
//        Long id2 = null;
//        try{
//            id2 = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try{
//            service.getServiceFriendship().removeFriendships(id1,id2);
//            System.out.println("Operatie realizata cu succes");
//        }catch(ArgumentException | NotInListException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void comunitatiUI(){
//        System.out.println("Nr de comunitati este: "+ service.getServiceFriendship().nrComunitati());
//    }
//
//    private void comunitateSociabilaUI(){
//        System.out.println("Cea mai sociabila comunitate este formata din: ");
//        System.out.println(service.getServiceFriendship().ceaMaiSociabilaComunitate());
//    }
//
//    private void listaUsersUI(){
//        service.getServiceUsers().listUsers().forEach(System.out::println);
//    }
//
//    private void listaFriendshipsUI(){
//        service.getServiceFriendship().listFriendships().forEach(System.out::println);
//    }
//
//
//    ////////////////////////////////////// CARDURI /////////////////////////////////////////////////
//    private void addCardUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce numele cardului:");
//        String nume = scanner.nextLine();
//
//        System.out.println("Introduce tipul cardului:");
//        String tip =  scanner.next();
//
//        try {
//            service.getServiceCard().addCard(tip, nume);
//            System.out.println("Operatie realizata cu succes");
//        }catch(ValidatorException | AlreadyInRepoException | ArgumentException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void removeCardUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul cardului:");
//
//        Long id = null;
//        try {
//            id = scanner.nextLong();
//        }catch (InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//        try {
//            service.getServiceCard().removeCard(id);
//            System.out.println("Operatie realizata cu succes");
//        }catch(ArgumentException | NotInListException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void listaCarduriUI(){
//        service.getServiceCard().getCarduri().forEach(System.out::println);
//    }
//
//    private void addRataCardUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul cardului:");
//
//        Long idCard = null;
//        try{
//            idCard = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        System.out.println("Introduce id-ul ratei pe care vrei sa o adaugi:");
//
//        Long idRata = null;
//        try{
//            idRata = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try {
//            service.getServiceCard().addRataCard(idCard, idRata);
//            System.out.println("Operatie realizata cu succes!");
//        }catch(ArgumentException | AlreadyInRepoException | NotInListException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private  void removeRataCardUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul cardului:");
//
//        Long idCard = null;
//        try{
//            idCard = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        System.out.println("Introduce id-ul ratei pe care vrei sa o elimini:");
//
//        Long idRata = null;
//        try{
//            idRata = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try {
//            service.getServiceCard().removeRataCard(idCard, idRata);
//            System.out.println("Operatie realizata cu succes!");
//        }catch(NotInListException | ArgumentException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void getPerformantaUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul cardului:");
//
//        Long idCard = null;
//        try{
//            idCard = scanner.nextLong();
//        }catch(InputMismatchException e) {
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try{
//            AbstractMap.SimpleEntry<Double,Double> perf = service.getServiceCard().getPerformantaMedieService(idCard);
//            System.out.println("Media rezistentelor este " + perf.getKey());
//            System.out.println("Media vitezelor este " + perf.getValue());
//        }catch(ArgumentException | NotInListException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void addEventUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce nr de culoare pe care le are bazinul:");
//        Integer nr = null;
//        try{
//            nr = scanner.nextInt();
//        }catch(InputMismatchException e){
//            System.out.println("Numarul trebuie sa fie intreg");
//            return;
//        }
//
//        try{
//            service.getServiceEvenimente().addEvent(nr);
//            System.out.println("Operatie realizata cu succes!");
//        }catch(AlreadyInRepoException | ValidatorException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//    private void removeEventUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul evenimentului:");
//
//        Long id = null;
//        try {
//            id = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try{
//            service.getServiceEvenimente().removeEvent(id);
//            System.out.println("Operatie realizata cu succes!");
//        }catch(NotInListException | ArgumentException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void listaEventsUI(){
//        service.getServiceEvenimente().getEvents().forEach(System.out::println);
//    }
//
//    private void addSubscriberToEventUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul evenimentului:");
//
//        Long idEvent = null;
//        try {
//            idEvent = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        System.out.println("Introduce id-ul utilizatorului:");
//
//        Long idUser = null;
//        try {
//            idUser = scanner.nextLong();
//        }catch(InputMismatchException e) {
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try{
//            service.getServiceEvenimente().subscribeToEvent(idEvent, idUser);
//            System.out.println("Operatie realizata cu succes!");
//        }catch(NotInListException | ArgumentException | AlreadyInRepoException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//    private void removeSubscriberFromEventUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul evenimentului:");
//
//        Long idEvent = null;
//        try {
//            idEvent = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        System.out.println("Introduce id-ul utilizatorului:");
//
//        Long idUser = null;
//        try {
//            idUser = scanner.nextLong();
//        }catch(InputMismatchException e) {
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try{
//            service.getServiceEvenimente().unsubscribeFromEvent(idEvent, idUser);
//            System.out.println("Operatie realizata cu succes!");
//        }catch(NotInListException | ArgumentException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void notifySubscribersUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul evenimentului:");
//
//        Long idEvent = null;
//        try {
//            idEvent = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        service.getServiceEvenimente().notifySubscribers(idEvent);
//    }
//
//    private void cursaUI(){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Introduce id-ul evenimentului:");
//
//        Long idEvent = null;
//        try {
//            idEvent = scanner.nextLong();
//        }catch(InputMismatchException e){
//            System.out.println("Id-ul trebuie sa fie nr intreg");
//            return;
//        }
//
//        try {
//            service.getServiceEvenimente().cursa(idEvent).forEach(System.out::println);
//        }catch(ArgumentException | NotInListException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void run(){
//        String option;
//        Scanner scanner = new Scanner(System.in);
//
//        while(true){
//            System.out.println("\n--------------------MENIU PRINCIPAL-----------------");
//            System.out.println("1 - Adauga un utilizator in lista");
//            System.out.println("2 - Sterge un utilizator din lista");
//            System.out.println("3 - Adauga o relatie de prietenie");
//            System.out.println("4 - Sterge o relatie de prietenie");
//            System.out.println("5 - Calculeaza nr de comunitati");
//            System.out.println("6 - Determina cea mai sociabila comunitate");
//            System.out.println("7 - Afiseaza lista de utilizatori");
//            System.out.println("8 - Afiseaza lista de frienships");
//            System.out.println("9 - Adauga un card");
//            System.out.println("10 - Sterge un card");
//            System.out.println("11 - Afiseaza lista de carduri");
//            System.out.println("12 - Adauga un eveniment");
//            System.out.println("13 - Sterge un eveniment");
//            System.out.println("14 - Afiseaza lista de evenimente");
//            System.out.println("card - Sublista cu operatii pe un card anume");
//            System.out.println("event - Sublista cu operatii pe un eveniment anume");
//            System.out.println("x - Iese din aplicatie\n");
//
//            System.out.println("Introduce optiunea:");
//            option = scanner.next();
//
//            if(Objects.equals(option, "x"))
//                break;
//            else if(option.equals("1"))
//                addUserUI();
//            else if(option.equals("2"))
//                removeUserUI();
//            else if(option.equals("3"))
//                addFriendshipUI();
//            else if(option.equals("4"))
//                removeFriendshipUI();
//            else if(option.equals("5"))
//                comunitatiUI();
//            else if(option.equals("6"))
//                comunitateSociabilaUI();
//            else if(option.equals("7"))
//                listaUsersUI();
//            else if(option.equals("8"))
//                listaFriendshipsUI();
//            else if(option.equals("9"))
//                addCardUI();
//            else if (option.equals("10"))
//                removeCardUI();
//            else if (option.equals("11"))
//                listaCarduriUI();
//            else if (option.equals("12"))
//                addEventUI();
//            else if(option.equals("13"))
//                removeEventUI();
//            else if(option.equals("14"))
//                listaEventsUI();
//            else if (option.equalsIgnoreCase("card")){
//                String option2;
//                while(true) {
//                    System.out.println("\n-------------- Submeniu Carduri --------------");
//                    System.out.println("a - Adauga o rata in card");
//                    System.out.println("b - Sterge o rata in card");
//                    System.out.println("c - Performanta");
//                    System.out.println("x - Iese din submeniu\n");
//
//                    System.out.println("Introduce optiunea:");
//                    option2 = scanner.next();
//
//                    if(option2.equalsIgnoreCase("a"))
//                        addRataCardUI();
//                    else if(option2.equalsIgnoreCase("b"))
//                        removeRataCardUI();
//                    else if(option2.equalsIgnoreCase("c"))
//                        getPerformantaUI();
//                    else if(option2.equalsIgnoreCase("x"))
//                        break;
//                    else System.out.println("Optiunea introdusa este invalida!");
//                }
//            }
//            else if(option.equalsIgnoreCase("event")){
//                String option2;
//                while(true) {
//                    System.out.println("\n----------------- Submeniu Evenimente --------------------");
//                    System.out.println("a - Subscribe");
//                    System.out.println("b - Unsubscribe");
//                    System.out.println("c - Notify Subscribers");
//                    System.out.println("d - Ruleaza cursa");
//                    System.out.println("x - Iese din submeniu\n");
//
//
//                    System.out.println("Introduce optiunea:");
//                    option2 = scanner.next();
//
//                    if(option2.equalsIgnoreCase("a"))
//                        addSubscriberToEventUI();
//                    else if(option2.equalsIgnoreCase("b"))
//                        removeSubscriberFromEventUI();
//                    else if(option2.equalsIgnoreCase("c"))
//                        notifySubscribersUI();
//                    else if(option2.equalsIgnoreCase("d"))
//                        cursaUI();
//                    else if (option2.equalsIgnoreCase("x"))
//                        break;
//                    else System.out.println("Optiunea introdusa este invalida!");
//                }
//            }
//            else
//                System.out.println("Optiunea introdusa nu este valabila");
//        }
//    }
//}
