package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXButton;
import com.treluxcom.metier.Famille;
import com.treluxcom.utilitaire.Notification;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PlaqueQuantiteController implements Initializable {

    MainClientController main;

    Double prix;

    String location;

    Famille famille;
    @FXML
    private Label nom;
    @FXML
    private ImageView image;
    @FXML
    private TextField quantite;

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMain(MainClientController main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            quantite.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,4}([\\.]\\d{0})?")) {
                        quantite.setText(oldValue);
                    }
                }
            });
            
            new InitThread().start();
        } catch (Exception e) {
        }
    }

    class InitThread extends Thread {

        @Override
        public void run() {
            try {
                try {
                    Thread.currentThread().sleep(5);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(() -> {
                    nom.setText(famille.getNom());
                    try {
                        image.setImage(new Image("/images/ressourceMedia/" + location));
                    } catch (Exception e) {
                    }
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

    }

    @FXML
    private void btnExit(MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void btnAjouter(ActionEvent event) {
        if (quantite.getText().isEmpty() || quantite.getText()==null || Integer.parseInt(quantite.getText())<=0) {
            Notification.notificationErr("Alerte", "Veuillez mentionner la quantite");
        } else {
            if (main.hashPanier.containsKey(famille)) {
                main.hashPanier.replace(famille, Double.parseDouble(quantite.getText()));
            } else {
                main.hashPanier.put(famille, Double.parseDouble(quantite.getText()));
            }
            main.updatePanier();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }

}
