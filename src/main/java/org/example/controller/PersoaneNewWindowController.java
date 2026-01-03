package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.example.service.ServiceUtilizatori;
import org.example.utils.TipUtilizator;

public class PersoaneNewWindowController {

    private ServiceUtilizatori service;
    private UserController mainController;

    @FXML private TextField fieldUsername;
    @FXML private TextField fieldEmail;
    @FXML private TextField fieldPassword;
    @FXML private TextField fieldNume;
    @FXML private TextField fieldPrenume;
    @FXML private TextField fieldDataNasterii;
    @FXML private TextField fieldOcupatie;
    @FXML private TextField fieldNivelEmpatie;

    public void setService(ServiceUtilizatori service, UserController controller){
        this.service = service;
        this.mainController = controller;
    }
    @FXML
    public void handleAddPersoana(ActionEvent event){
        try {
            service.addUser(
                    TipUtilizator.PERSOANA,
                    fieldUsername.getText(),
                    fieldEmail.getText(),
                    fieldPassword.getText(),
                    fieldNume.getText(),
                    fieldPrenume.getText(),
                    fieldDataNasterii.getText(),
                    fieldOcupatie.getText(),
                    fieldNivelEmpatie.getText()
            );

            // Reîncarcă toate persoanele
            mainController.reloadPersons();

            closeWindow(event);

        } catch (Exception e){
            mainController.showError("Eroare in datele persoanei:\n" + e.getMessage());
        }
    }



    public void closeWindow(ActionEvent event){
        Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
