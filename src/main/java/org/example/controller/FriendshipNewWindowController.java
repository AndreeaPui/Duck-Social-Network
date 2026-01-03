package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.example.service.ServiceFriendship;

public class FriendshipNewWindowController {

    private ServiceFriendship serviceFriendship;
    private UserController mainController;

    @FXML private TextField fieldUser1;
    @FXML private TextField fieldUser2;

    public void setService(ServiceFriendship serviceFriendship, UserController controller) {
        this.serviceFriendship = serviceFriendship;
        this.mainController = controller;
    }

    @FXML
    public void handleAddFriendship(ActionEvent event) {
        try {
            Long id1 = Long.parseLong(fieldUser1.getText());
            Long id2 = Long.parseLong(fieldUser2.getText());

            serviceFriendship.addFriendships(id1, id2);

            mainController.reloadFriendships();
            closeWindow(event);

        } catch (Exception e) {
            mainController.showError("Eroare la adaugarea prieteniei:\n" + e.getMessage());
        }
    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}

