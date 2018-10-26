package com.treluxcom.controlleur;

import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.PropertiesManager;
import java.net.URL;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class ServerConfigurationController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private TextField adresseip;
    @FXML
    private AnchorPane parent;
    @FXML
    private TextField port;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            makeStageDrageable();

        } catch (Exception ex) {
            Logger.getLogger(ServerConfigurationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                PageInternauteController.stage.setX(event.getScreenX() - xOffset);
                PageInternauteController.stage.setY(event.getScreenY() - yOffset);
                PageInternauteController.stage.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            PageInternauteController.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            PageInternauteController.stage.setOpacity(1.0f);
        });

    }
    Registry registry;

    @FXML
    private void btnStop(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void btnDemarrer(ActionEvent event) {
        if (adresseip.getText().isEmpty()) {
            Notification.notificationErr("Erreur", "Renseigner l'adresse ip du Serveur");
        } else if (port.getText().isEmpty()) {
            Notification.notificationErr("Erreur", "Renseigner le numero de port du Serveur");
        } else {
              PropertiesManager.createPropertiesFile(adresseip.getText(), Integer.parseInt(port.getText()));
              System.exit(0);
        }

    }

}
