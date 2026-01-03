package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.domain.User;
import org.example.service.ServiceFriendship;
import org.example.service.ServiceFriendshipRequest;
import org.example.service.ServiceUtilizatori;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddFriendController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button nextPageButton;

    private ServiceUtilizatori userService;
    private ServiceFriendship friendshipService;
    private ServiceFriendshipRequest friendsRQservice;
    private User currentUser;
    private ObservableList<User> availableUsers;
    private int currentPage = 1;
    private final int pageSize = 10;

    public void setServices(ServiceUtilizatori userService, ServiceFriendship friendshipService, ServiceFriendshipRequest friendsRQservice, User currentUser) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendsRQservice = friendsRQservice;
        this.currentUser = currentUser;
        loadUsers();
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    private void loadUsers() {
        // 1. Încărcați toți utilizatorii din service
        List<User> allUsers = StreamSupport.stream(userService.findAll().spliterator(), false)
                .collect(Collectors.toList());
        System.out.println("All Users: " + allUsers);

        // 2. Încărcați prietenii și cererile (logica existentă)
        List<User> friends = friendshipService.findFriends(currentUser.getId(), 1, Integer.MAX_VALUE);
        System.out.println("Friends: " + friends);

        List<User> friendRequests = friendsRQservice.getFriendRequestsUser(currentUser.getId());
        System.out.println("Friend Requests: " + friendRequests);

        // 3. FILTRAREA CORECTATĂ: Adăugarea filtrului pentru null (user != null)
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> user != null) // <-- NOU: Ignoră utilizatorii nuli
                .filter(user -> !friends.contains(user) &&
                        !friendRequests.contains(user) &&
                        !user.equals(currentUser))
                .collect(Collectors.toList());
        System.out.println("Available Users: " + filteredUsers);

        // 4. Paginarea (logica existentă)
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredUsers.size());
        List<User> paginatedUsers = filteredUsers.subList(fromIndex, toIndex);

        availableUsers = FXCollections.observableArrayList(paginatedUsers);
        usersTable.setItems(availableUsers);

        prevPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(toIndex >= filteredUsers.size());
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            loadUsers();
        }
    }

    @FXML
    private void handleNextPage() {
        currentPage++;
        loadUsers();
    }

    @FXML
    private void handleAddFriend() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            friendsRQservice.sendFriendRequest(currentUser.getId(), selectedUser.getId());
            availableUsers.remove(selectedUser);
        }
    }
}