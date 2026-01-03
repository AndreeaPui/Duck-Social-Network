package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.service.ServiceFriendship;
import org.example.service.ServiceFriendshipRequest;
import org.example.service.ServiceMessages;
import org.example.service.ServiceUtilizatori;

import java.io.IOException;

public class StartController {

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button confirmButton;

    private ServiceUtilizatori userService;
    private ServiceFriendship friendshipService;
    private ServiceFriendshipRequest friendshipRQService;
    private ServiceMessages messageService; // Add this field

    // Public no-argument constructor
    public StartController() {
    }

    @FXML
    public void initialize() {
        // Initialize the logo image if needed
        // logoImageView.setImage(new Image("path/to/logo.png"));
    }

    @FXML
    private void handleGetStartedButton() {
        openLoginPage();
        closeCurrentPage();
    }

    private void openLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            VBox loginPane = loader.load();

            LoginController loginController = loader.getController();
            loginController.setUserService(userService, friendshipService, friendshipRQService, messageService); // Update this line

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(loginPane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentPage() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void setService(ServiceUtilizatori userService, ServiceFriendship friendshipService, ServiceFriendshipRequest serviceFriendshipRequest, ServiceMessages messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRQService = serviceFriendshipRequest;
        this.messageService = messageService;
    }
}