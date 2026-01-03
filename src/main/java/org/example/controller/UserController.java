package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.domain.Persoana;
import org.example.domain.Rata;
import org.example.domain.User;
import org.example.domain.Friendship;
import org.example.service.ServiceMessages;
import org.example.service.ServiceUtilizatori;
import org.example.service.ServiceFriendship;
import org.example.repository.dto.Page;
import org.example.repository.dto.Pageable;
import org.example.utils.TipRata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private User loggedInUser;

    private ServiceUtilizatori service;
    private ServiceFriendship serviceFriendship;
    private ServiceMessages serviceMessages;

    private int pageSizeDucks = 4;
    private int currentPageDucks = 0;

    private int pageSizePersons = 4;
    private int currentPagePersons = 0;

    private int pageSizeFriendships = 4;
    private int currentPageFriendships = 0;

    // --- TableViews Ducks ---
    @FXML private TableView<Rata> tableDucks;
    @FXML private TableColumn<Rata, Long> colDuckId;
    @FXML private TableColumn<Rata, String> colDuckUsername;
    @FXML private TableColumn<Rata, String> colDuckEmail;
    @FXML private TableColumn<Rata, String> colDuckPassword;
    @FXML private TableColumn<Rata, TipRata> colDuckTip;
    @FXML private TableColumn<Rata, Double> colDuckViteza;
    @FXML private TableColumn<Rata, Double> colDuckRezistenta;
    @FXML private TableColumn<Rata, Long> colDuckCard;
    @FXML private ComboBox<TipRata> comboDuckTip;
    @FXML private Label labelPageDucks;

    // --- TableViews Persons ---
    @FXML private TableView<User> tablePersons;
    @FXML private TableColumn<User, Long> colPersonId;
    @FXML private TableColumn<User, String> colPersonUsername;
    @FXML private TableColumn<User, String> colPersonEmail;
    @FXML private TableColumn<User, String> colPersonPassword;
    @FXML private TableColumn<User, String> colPersonNume;
    @FXML private TableColumn<User, String> colPersonPrenume;
    @FXML private TableColumn<User, String> colPersonDataNasterii;
    @FXML private TableColumn<User, String> colPersonOcupatie;
    @FXML private TableColumn<User, Integer> colPersonNivelEmpatie;
    @FXML private Label labelPagePersons;

    // --- TableViews Friendships ---
    @FXML private TableView<Friendship> tableFriendships;
    @FXML private TableColumn<Friendship, Long> colFriendshipId;
    @FXML private TableColumn<Friendship, Long> colUser1;
    @FXML private TableColumn<Friendship, Long> colUser2;
    @FXML private Label labelPageFriendships;

    // --- Models ---
    ObservableList<Rata> modelDucks = FXCollections.observableArrayList();
    ObservableList<User> modelPersons = FXCollections.observableArrayList();
    ObservableList<Friendship> modelFriendships = FXCollections.observableArrayList();

    // ==================== SET SERVICE ====================
    public void setService(ServiceUtilizatori service, ServiceFriendship serviceFriendship, ServiceMessages serviceMessages, User loggedInUser) {
        this.service = service;
        this.serviceFriendship = serviceFriendship;
        this.serviceMessages = serviceMessages;
        this.loggedInUser = loggedInUser; // Salvează utilizatorul logat

        // Adăugat o coloană lipsă la Ducks (presupunând că exista în FXML)
        if (colDuckCard != null) {
            colDuckCard.setCellValueFactory(new PropertyValueFactory<>("card"));
        }

        loadPageDucks(0);
        loadPagePersons(0);
        loadPageFriendships(0);
    }

    // ==================== INITIALIZE ====================
    @FXML
    public void initialize() {
        // Ducks
        colDuckId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDuckUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colDuckEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDuckPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colDuckTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        colDuckViteza.setCellValueFactory(new PropertyValueFactory<>("viteza"));
        colDuckRezistenta.setCellValueFactory(new PropertyValueFactory<>("rezistenta"));
        tableDucks.setItems(modelDucks);
        comboDuckTip.getItems().add(null);
        comboDuckTip.getItems().addAll(TipRata.values());

        // Persons
        colPersonId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPersonUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPersonEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPersonPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colPersonNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colPersonPrenume.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        colPersonDataNasterii.setCellValueFactory(new PropertyValueFactory<>("dataNasterii"));
        colPersonOcupatie.setCellValueFactory(new PropertyValueFactory<>("ocupatie"));
        colPersonNivelEmpatie.setCellValueFactory(new PropertyValueFactory<>("nivelEmpatie"));
        tablePersons.setItems(modelPersons);

        // Friendships
        colFriendshipId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUser1.setCellValueFactory(new PropertyValueFactory<>("user1"));
        colUser2.setCellValueFactory(new PropertyValueFactory<>("user2"));
        tableFriendships.setItems(modelFriendships);
    }

    // ==================== Ducks ====================
    private void loadPageDucks(int pageNumber) {
        TipRata tip = comboDuckTip.getValue();
        Pageable pageable = new Pageable(pageNumber, pageSizeDucks);
        Page<User> page = service.findAllRateOnPage(pageable, tip);

        List<Rata> ducks = new ArrayList<>();
        for (User u : page.getElements()) {
            if (u instanceof Rata r) ducks.add(r);
        }
        modelDucks.setAll(ducks);
        labelPageDucks.setText("Current Page: " + (pageNumber + 1));
    }

    @FXML public void handleFilterDucks() { currentPageDucks = 0; loadPageDucks(currentPageDucks); }
    @FXML public void previousPageDucks() { if(currentPageDucks>0) currentPageDucks--; loadPageDucks(currentPageDucks); }
    @FXML public void nextPageDucks() { currentPageDucks++; loadPageDucks(currentPageDucks); }
    @FXML public void handleDeleteDuck() { Rata selected = tableDucks.getSelectionModel().getSelectedItem(); if(selected!=null){ service.removeUser(selected.getId()); loadPageDucks(currentPageDucks); } }

    @FXML
    public void openNewDuckWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewWindowDuck.fxml"));
            Parent root = loader.load();
            RateNewWindowController controller = loader.getController();
            controller.setService(service, this);
            Stage stage = new Stage();
            stage.setTitle("Add new Duck");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void reloadDucks() { loadPageDucks(currentPageDucks); }


    @FXML
    public void openChatWindow() {
        // 1. Obține utilizatorul selectat din TableView-ul curent (Partenerul de chat)
        User selectedUser = tableDucks.getSelectionModel().getSelectedItem(); // Încearcă tab-ul Ducks

        if (selectedUser == null) {
            // Încearcă tab-ul Persons dacă nu s-a selectat nimic din Ducks
            selectedUser = tablePersons.getSelectionModel().getSelectedItem();
        }

        if (selectedUser == null) {
            showError("Please select a user (Duck or Person) to chat with.");
            return;
        }

        if (selectedUser.getId().equals(loggedInUser.getId())) {
            showError("You cannot chat with yourself.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chat.fxml"));
            Parent root = loader.load();
            ChatController controller = loader.getController();

            // 2. Apel corect: Trimite toate cele 3 argumente necesare
            controller.setServices(serviceMessages, loggedInUser, selectedUser);

            Stage stage = new Stage();
            stage.setTitle("Chat with " + selectedUser.getUsername());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showError("Error loading chat window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== Persons ====================
    private void loadPagePersons(int pageNumber) {
        Pageable pageable = new Pageable(pageNumber, pageSizePersons);
        Page<User> page = service.findAllPersOnPage(pageable);

        List<User> persons = new ArrayList<>();
        for (User u : page.getElements()) if (u instanceof Persoana) persons.add(u);
        modelPersons.setAll(persons);
        labelPagePersons.setText("Current Page: " + (pageNumber + 1));
    }


    @FXML public void openNewPersonWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewWindowPerson.fxml"));
            Parent root = loader.load();
            PersoaneNewWindowController controller = loader.getController();
            controller.setService(service, this);
            Stage stage = new Stage();
            stage.setTitle("Add new Person");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML public void handleDeletePerson() {
        User selected = tablePersons.getSelectionModel().getSelectedItem();
        if(selected!=null){
            service.removeUser(selected.getId());
            loadPagePersons(currentPagePersons);
        }
    }

    public void reloadPersonsAfterAdd() {
        // filtrăm doar persoanele
        int totalPersons = 0;
        for (User u : service.getAllUsers()) {
            if (!(u instanceof Rata)) totalPersons++;
        }
        currentPagePersons = (totalPersons - 1) / pageSizePersons;
        loadPagePersons(currentPagePersons);
    }

    @FXML public void previousPagePersons() { if(currentPagePersons>0) currentPagePersons--; loadPagePersons(currentPagePersons); }
    @FXML public void nextPagePersons() { currentPagePersons++; loadPagePersons(currentPagePersons); }
    public void reloadPersons() {
//        List<User> allUsers = service.getAllUsers(); // presupun că returnează List<User>
//        List<User> persons = new ArrayList<>();
//        for(User u : allUsers){
//            if(!(u instanceof Rata)) persons.add(u);
//        }
//        modelPersons.setAll(persons);
        loadPagePersons(currentPagePersons);
    }


    // ==================== Friendships ====================
    private void loadPageFriendships(int pageNumber) {
        Pageable pageable = new Pageable(pageNumber, pageSizeFriendships);
//        Page<Friendship> page = serviceFriendship.findFriendshipsOnPage(pageable);
        List<Friendship> list = new ArrayList<>();
//        page.getElements().forEach(list::add);
        modelFriendships.setAll(list);
        labelPageFriendships.setText("Current Page: " + (pageNumber + 1));
    }

    @FXML
    public void openNewFriendshipWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewWindowFriendship.fxml"));
            Parent root = loader.load();

            FriendshipNewWindowController controller = loader.getController();
            controller.setService(serviceFriendship, this);

            Stage stage = new Stage();
            stage.setTitle("Add Friendship");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteFriendship() {
        Friendship selected = tableFriendships.getSelectionModel().getSelectedItem();
        if (selected != null) {
            serviceFriendship.removeFriendships(selected.getUser1(), selected.getUser2());
            loadPageFriendships(currentPageFriendships);
        }
    }

    public void reloadFriendships() {
        loadPageFriendships(currentPageFriendships);
    }


    @FXML
    public void previousPageFriendships() {
        if(currentPageFriendships>0)
            currentPageFriendships--;
        loadPageFriendships(currentPageFriendships);
    }

    @FXML
    public void nextPageFriendships() {
        currentPageFriendships++;
        loadPageFriendships(currentPageFriendships);
    }

    public void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare");
        alert.setHeaderText(null); // ca să fie mai compactă fereastra
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleMostSociableCommunity() {
        try {
            List<Long> community = serviceFriendship.ceaMaiSociabilaComunitate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Most Sociable Community");
            alert.setHeaderText("Users in the most sociable community:");
            alert.setContentText(community.toString());
            alert.showAndWait();

        } catch (Exception e) {
            showError("Error calculating sociable community: " + e.getMessage());
        }
    }

    @FXML
    public void handleNrCommunities() {
        try {
            int nr = serviceFriendship.nrComunitati();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Number of Communities");
            alert.setHeaderText("Total communities found:");
            alert.setContentText(String.valueOf(nr));
            alert.showAndWait();

        } catch (Exception e) {
            showError("Error calculating number of communities: " + e.getMessage());
        }
    }


}
