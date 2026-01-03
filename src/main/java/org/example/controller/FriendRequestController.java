package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.domain.User;
import org.example.service.ServiceFriendshipRequest;

import java.util.List;
import java.util.stream.Collectors;

public class FriendRequestController {

    @FXML
    private TableView<User> friendRequestsTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    private ServiceFriendshipRequest friendshipRequestService;
    private User currentUser;
    private ObservableList<User> friendRequests;

    public void setServices(ServiceFriendshipRequest friendshipRequestService, User currentUser) {
        this.friendshipRequestService = friendshipRequestService;
        this.currentUser = currentUser;
        loadFriendRequests();
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        friendRequests = FXCollections.observableArrayList();
        friendRequestsTable.setItems(friendRequests);
    }

    private void loadFriendRequests() {
        List<User> requests = friendshipRequestService.getFriendRequestsUser(currentUser.getId());
        requests = requests.stream()
                .filter(user -> !user.equals(currentUser))
                .collect(Collectors.toList());
        friendRequests.setAll(requests);
    }

    @FXML
    private void handleAccept() {
        User selectedUser = friendRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            friendshipRequestService.acceptFriendRequest(currentUser.getId(), selectedUser.getId());
            friendRequests.remove(selectedUser);
        }
    }

    @FXML
    private void handleDeny() {
        User selectedUser = friendRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            friendshipRequestService.denyFriendRequest(currentUser.getId(), selectedUser.getId());
            friendRequests.remove(selectedUser);
        }
    }
}