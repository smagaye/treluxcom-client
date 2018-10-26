/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.controlleur;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ADA-MALICK
 */
public class MainLivreurController implements Initializable {

    @FXML
    private Label prenom;
    @FXML
    private Label nom;
    @FXML
    private Label adresse;
    @FXML
    private Label telephone;
    @FXML
    private Label nombreProduit;
    @FXML
    private Label montant;
    @FXML
    private ScrollPane scrollPaneLivreur;
    @FXML
    private VBox vboxClient;
    @FXML
    private AnchorPane parent;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Initializes the controller class.
     */
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
                ConnexionController.stageBack.setX(event.getScreenX() - xOffset);
                ConnexionController.stageBack.setY(event.getScreenY() - yOffset);
                ConnexionController.stageBack.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            ConnexionController.stageBack.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            ConnexionController.stageBack.setOpacity(1.0f);
        });

    }

    @FXML
    private void btnAnnuler(ActionEvent event) {
    }

    @FXML
    private void btnLivrer(ActionEvent event) {
    }

    @FXML
    private void btnFacture(ActionEvent event) {
    }

    @FXML
    private void btnExit(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

}
