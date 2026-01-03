package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.exceptii.EntityMissingException;
import org.example.service.ServiceFriendship;
import org.example.service.ServiceFriendshipRequest;
import org.example.service.ServiceMessages;
import org.example.service.ServiceUtilizatori;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private ServiceUtilizatori userService;
    private ServiceFriendship friendshipService;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    private ServiceMessages messageService; // Add this field

    public void setUserService(ServiceUtilizatori userService, ServiceFriendship friendshipService, ServiceFriendshipRequest friendshipRQService, ServiceMessages messageService) { // Update this line
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.serviceFriendshipRequest = friendshipRQService;
        this.messageService = messageService; // Add this line
    }

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();


        if (username != null && password != null) {
            try {
                User user = userService.findUserByUsername(username);
                if (!userService.verifyCredentials(user, password)) {
                    showInformationAlert("Login Failed", "Operation Failed", "Wrong username or password.");
                    return;
                }

                openUserPage(user);
                closePage((Stage) loginButton.getScene().getWindow());

            } catch (EntityMissingException e) {
                System.out.println(e.getMessage());
                showInformationAlert("Login Failed", "Invalid Credentials", e.getMessage());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closePage(Stage stage) {
        stage.close();
    }

    private void openUserPage(User user) throws IOException {
        System.out.println(user);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_page.fxml"));
        Scene scene = new Scene(loader.load());

        HomePageController homePageController = loader.getController();

        homePageController.setUserService(userService);
        homePageController.setFriendshipService(friendshipService);
        homePageController.setMessageService(messageService);
        homePageController.setFriendRequestsService(serviceFriendshipRequest);
        homePageController.setCurrentUser(user);

        Stage stage = new Stage();
        stage.setTitle("Home Page");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openAddPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create-account.fxml"));
        Scene scene = new Scene(loader.load());

        SingInController signInPageController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Create New Account");
        stage.setScene(scene);
        stage.show();
    }
}