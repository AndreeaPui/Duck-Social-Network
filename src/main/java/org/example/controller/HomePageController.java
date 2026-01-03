package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.domain.User;
import org.example.observers.Observer;
import org.example.service.ServiceFriendship;
import org.example.service.ServiceFriendshipRequest;
import org.example.service.ServiceMessages;
import org.example.service.ServiceUtilizatori;

import java.io.IOException;
import java.util.List;

public class HomePageController implements Observer {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<User> friendsTable;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button removeFriendButton;

    @FXML
    private Button friendRequestsButton;

    @FXML
    private Button notificationsButton;

    @FXML
    private Button chatButton;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button nextPageButton;

    private ServiceUtilizatori userService;
    private ServiceFriendship friendshipService;
    private ServiceFriendshipRequest friendsRQservice;
    private ServiceMessages messageService;
    private User currentUser;
    private ObservableList<User> friendsList = FXCollections.observableArrayList();
    private int currentPage = 1;
    private final int pageSize = 5;

    public void setUserService(ServiceUtilizatori userService) {
        this.userService = userService;
    }

    public void setMessageService(ServiceMessages messageService) {
        this.messageService = messageService;
    }


    public void setFriendshipService(ServiceFriendship friendshipService) {
        this.friendshipService = friendshipService;
        this.friendshipService.addObserver(this);
    }

    public void setFriendRequestsService(ServiceFriendshipRequest friendsRQservice) {
        this.friendsRQservice = friendsRQservice;
        this.friendsRQservice.addObserver(this);
    }

    @Override
    public void update() {
        loadFriends();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
        loadFriends();
    }

    @FXML
    public void initialize() {
        // COLONNA 1: Afișează Username-ul (Presupunem că User.getUsername() există)
        firstNameColumn.setText("Username"); // Schimbăm header-ul vizual
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // COLONNA 2: Afișează Tipul Utilizatorului (Persoana sau Rata)
        lastNameColumn.setText("Tip Utilizator"); // Schimbăm header-ul vizual

        lastNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> cellData) {
                User user = cellData.getValue();

                // Determinăm tipul utilizatorului folosind getClass()
                String userType = user.getClass().getSimpleName();

                // Returnăm tipul ca String observabil
                return new SimpleStringProperty(userType);
            }
        });
    }

    private void loadFriends() {
        List<User> friends = friendshipService.findFriends(currentUser.getId(), currentPage, pageSize);
        friendsList.setAll(friends);
        friendsTable.setItems(friendsList);

        prevPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(friends.size() < pageSize);
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            loadFriends();
        }
    }

    @FXML
    private void handleNextPage() {
        currentPage++;
        loadFriends();
    }

    @FXML
    private void handleAddFriend() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add-friend-view.fxml"));
            AnchorPane addFriendPane = loader.load();

            AddFriendController addFriendController = loader.getController();
            addFriendController.setServices(userService, friendshipService, friendsRQservice, currentUser);

            Stage stage = new Stage();
            stage.setTitle("Add Friend");
            stage.setScene(new Scene(addFriendPane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to Load Add Friend Page", "An error occurred while trying to load the Add Friend page: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            showAlert("Error", "Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleRemoveFriend() {
        User selectedUser = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                friendshipService.removeFriendships(currentUser.getId(), selectedUser.getId());
                loadFriends();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to Remove Friend", "An error occurred while trying to remove the friend: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "No User Selected", "Please select a user in the table.");
        }
    }

    @FXML
    private void handleFriendRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/friend-request-view.fxml"));
            Parent root = loader.load();

            FriendRequestController controller = loader.getController();
            controller.setServices(friendsRQservice, currentUser);

            Stage stage = new Stage();
            stage.setTitle("Friend Requests");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNotifications() {
        try {
            if (friendsRQservice == null) {
                throw new RuntimeException("FriendshipRequestService is not initialized");
            }
            boolean hasPendingRequests = friendsRQservice.hasPendingRequests(currentUser.getId());
            showNotification(hasPendingRequests);
        } catch (Exception e) {
            logError(e);
            showAlert("Error", "An error occurred", e.getMessage());
        }
    }

    private void showNotification(boolean hasPendingRequests) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/notification.fxml"));
            Parent root = loader.load();
            NotificationController controller = loader.getController();

            if (hasPendingRequests) {
                controller.setTitle("Friend Requests");
                controller.setMessage("You have unanswered friend requests. Check them out!");
            } else {
                controller.setTitle("Friend Requests");
                controller.setMessage("No new friend requests.");
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logError(e);
        }
    }

    private void logError(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace(); // Consider using a logging framework
    }

    @FXML
    private void handleChat() {
        User selectedUser = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chat.fxml"));
                Parent root = loader.load();

                ChatController chatController = loader.getController();
                chatController.setServices(messageService, currentUser, selectedUser);

                Stage stage = new Stage();
                stage.setTitle("Chat with " + selectedUser.getUsername());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to Load Chat Page", "An error occurred while trying to load the Chat page: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "No User Selected", "Please select a user in the table.");
        }
    }

//    @FXML
//    private void handleProfile() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/socialnetworkfx/fxml/profile.fxml"));
//            Parent root = loader.load();
//
//            ProfileController controller = loader.getController();
//            controller.setUserService(userService); // Set the UserService
//            controller.setUser(currentUser);
//
//            Stage stage = new Stage();
//            stage.setTitle("User Profile");
//            stage.setScene(new Scene(root, 360, 640));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//            showAlert("Error", "Failed to Load Profile Page", "An error occurred while trying to load the Profile page: " + e.getMessage());
//        }
//    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}