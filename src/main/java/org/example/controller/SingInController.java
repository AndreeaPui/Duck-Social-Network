package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.service.ServiceUtilizatori;

import java.io.IOException;

public class SingInController {
    @FXML
    private Button duckloginButton;

    @FXML
    private Button personloginButton;

    private ServiceUtilizatori service;

    @FXML
    public void openNewDuckWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewWindowDuck.fxml"));
            Parent root = loader.load();
            RateNewWindowController controller = loader.getController();
            //controller.setService(service, this);
            Stage stage = new Stage();
            stage.setTitle("Add new Duck");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
