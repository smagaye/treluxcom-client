package com.treluxcom.controlleur;

import com.treluxcom.metier.Famille;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.WindowManager;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PlaquesMesPaniersController implements Initializable {

    @FXML
    private VBox vBoxPanier = new VBox();

    public void setMain(MainClientController main) {
        this.main = main;
    }
    
        public MainClientController getMain() {
        return this.main = main;
    }

    public void setHashPanier(HashMap<Famille, Double> hashPanier) {
        this.hashPanier = hashPanier;
    }

    public void setHashPrice(HashMap<Famille, Double> hashPrice) {
        this.hashPrice = hashPrice;
    }

    MainClientController main;

    HashMap<Famille, Double> hashPanier = new HashMap<Famille, Double>();

    HashMap<Famille, Double> hashPrice = new HashMap<Famille, Double>();

    Double prixTotal=0.0;
    @FXML
    private void btnExit(MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
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
                    chargerProduit();
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        private void chargerProduit() {
            try {
                int i = 1;
                
                vBoxPanier.setPrefHeight(80 * hashPanier.size());
                for (Map.Entry<Famille, Double> entry : hashPanier.entrySet()) {
                    Famille key = entry.getKey();
                    Double value = entry.getValue();
                    HBox plaque = new HBox();
                    plaque.setPrefHeight(48);
                    plaque.setPrefWidth(372);

                    Label num = new Label();
                    Label nom = new Label();
                    Label prix = new Label();
                    num.setAlignment(Pos.CENTER);
                    num.setPrefHeight(44);
                    num.setPrefWidth(53);

                    nom.setAlignment(Pos.CENTER);
                    nom.setPrefHeight(44);
                    nom.setPrefWidth(190);

                    prix.setAlignment(Pos.CENTER);
                    prix.setPrefHeight(44);
                    prix.setPrefWidth(132);

                    num.setText(Integer.toString(i));
                    nom.setText(key.getNom());
                    prix.setText(hashPrice.get(key).toString() + " FCFA");

                    plaque.getChildren().addAll(num, nom, prix);
                    vBoxPanier.getChildren().add(plaque);
                    prixTotal =prixTotal+ hashPanier.get(key) * hashPrice.get(key);
                    i++;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new InitThread().start();
    }

    @FXML
    private void btnAcheterPanier(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fxml/PlaquePaiement.fxml"));
            Parent root = (Parent) chargeur.load();
            Scene scene = new Scene(root);
            PlaquePaiementController plaque = chargeur.getController();
            plaque.setMain(this);
            plaque.setHashPanier(hashPanier);
            plaque.setHashPrice(hashPrice);
            plaque.setMontantTotal(prixTotal.doubleValue());
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();           
            WindowManager.closeWindow(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void viderPanier(ActionEvent event) {
        main.hashPanier.clear();
        main.updatePanier();
        WindowManager.closeWindow(event);
    }

}
