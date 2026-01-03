package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.example.service.ServiceUtilizatori;
import org.example.utils.TipRata;
import org.example.utils.TipUtilizator;

public class RateNewWindowController {

    private ServiceUtilizatori service;
    private UserController mainController;
    private SingInController singInController;
    @FXML private TextField fieldUsername;
    @FXML private TextField fieldEmail;
    @FXML private TextField fieldPassword;
    @FXML private TextField fieldViteza;
    @FXML private TextField fieldRezistenta;
    @FXML private ComboBox<TipRata> comboTip;

    public void setService(ServiceUtilizatori service, UserController controller){
        this.service = service;
        this.mainController = controller;
        comboTip.getItems().addAll(TipRata.values());
    }

    @FXML
    public void handleAddRata(ActionEvent event){
        try {
            String user = fieldUsername.getText();
            String email = fieldEmail.getText();
            String pass = fieldPassword.getText();
            TipRata tip = comboTip.getValue();

            if (tip == null) {
                System.out.println("Selectează tipul ratei!");
                return;
            }

            // apel corect catre SERVICE
            service.addUser(
                    TipUtilizator.RATA,
                    user,
                    email,
                    pass,
                    tip.toString(),
                    fieldViteza.getText(),
                    fieldRezistenta.getText()
            );

            mainController.reloadDucks();  // actualizează tabelul din fereastra principală
            closeWindow(event);

        } catch (Exception e){
            mainController.showError("Eroare introducere rata:\n" + e.getMessage());
        }
    }

    public void closeWindow(ActionEvent event){
        Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}