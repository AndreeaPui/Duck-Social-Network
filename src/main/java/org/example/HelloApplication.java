package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.StartController;
import org.example.domain.*;
import org.example.repository.PagedRepository;
import org.example.repository.database.*;
import org.example.service.*;
import org.example.validatori.Validate;

import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {

    // Configurarea conexiunii
    private final String url = "jdbc:postgresql://localhost:5432/DuckNetwork";
    private final String user = "postgres";
    private final String pass = "postgres";

    // Validatori și Repository-uri
    private Validate validator;
    private RepoDBUtilizatori repoUsers;
    private RepoDBFriendships repoFriendships;
    private RepoDBMessages repoMessages; // ADAUGAT
    private RepoDBFriendshipRequest repoFriendshipRequest;
    private PagedRepository<Card<? extends Rata>> repoCard;
    //private PagedRepository<Event> repoEvent;
    private RepoDBUserEvent repoDBUserEvent;

    // Service-uri
    private ServiceFriendship serviceFriendship;
    private ServiceMessages serviceMessages; // ADAUGAT
    private ServiceCard serviceCard;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    //private ServiceEvenimente serviceEvenimente;
    private ServiceUtilizatori service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Reading data from database...");

        // 1. INSTANTIEREA VALIDATORULUI
        validator = new Validate();

        // 2. INSTANTIEREA REPOSITORY-URILOR
        repoUsers = new RepoDBUtilizatori(url, user, pass);
        repoFriendships = new RepoDBFriendships(url, user, pass,repoUsers);
        repoCard = new RepoDBCard(url, user, pass);
        //repoEvent = new RepoDBEvent(url, user, pass);
        repoDBUserEvent = new RepoDBUserEvent(url, user, pass);

        // PAS CRITIC ADĂUGAT: Inițializarea Repository-ului de Mesaje
        // Presupunând că RepoDBMessages folosește același constructor (url, user, pass)
        repoMessages = new RepoDBMessages(url, user, pass);

        // 3. INSTANTIEREA SERVICE-URILOR (ORDINEA CONTEAZĂ)
        serviceFriendship = new ServiceFriendship(repoUsers, repoFriendships);
        serviceCard = new ServiceCard(repoCard, repoUsers, validator);
        //serviceEvenimente = new ServiceEvenimente(repoEvent, repoUsers, repoDBUserEvent, validator);

        // PAS CRITIC ADĂUGAT: Inițializarea Service-ului de Mesaje
        // Aici serviceMessages nu mai este null!
        serviceMessages = new ServiceMessages(repoMessages);

        // ServiceUtilizatori depinde de celelalte Service-uri
        service = new ServiceUtilizatori(repoUsers, serviceFriendship, validator);

        repoFriendshipRequest = new RepoDBFriendshipRequest(url, user, pass, repoUsers);
        serviceFriendshipRequest = new ServiceFriendshipRequest(repoFriendshipRequest, repoUsers, repoFriendships);
        // 4. INIȚIALIZAREA VEDERII PRINCIPALE (Login)
        initView(primaryStage);

        primaryStage.setTitle("DuckSocialNetwork Login");
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        // Presupunând că 'hello-view.fxml' este FXML-ul pentru Login (StartController)
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxml/hello-view.fxml"));

        VBox userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        // Preluarea Controller-ului (care este LoginController-ul tău, numit StartController aici)
        StartController startController = fxmlLoader.getController();

        // INJECȚIA FINALĂ: Trimiterea tuturor instanțelor de Service
        // Acum serviceMessages NU MAI ESTE NULL
        startController.setService(service, serviceFriendship,serviceFriendshipRequest, serviceMessages);
    }
}