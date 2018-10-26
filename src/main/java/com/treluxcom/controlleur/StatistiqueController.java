/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.controlleur;

import com.treluxcom.service.Statistiques;
import com.treluxcom.utilitaire.Reseau;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class StatistiqueController implements Initializable {

    @FXML
    private AnchorPane dashbordAdmin;
    @FXML
    private Pane stats;
    @FXML
    private Label statut;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private Label statut1;
    @FXML
    private PieChart piechart;

    private Statistiques statistiques;
    private HashMap<String , Integer> dashBoard;
    @FXML
    private Label idBoutique;
    @FXML
    private Label idEmploye;
    @FXML
    private Label idFournisseur;
    @FXML
    private Label idClient;
    @FXML
    private Label idMessage;
    @FXML
    private Label idProduitEnStock;
    @FXML
    private Label idProduitEnStockPercent;
    @FXML
    private Label idProduitVendu;
    @FXML
    private Label idProduitVenduPercent;
    @FXML
    private Label idProduitExpire;
    @FXML
    private Label idProduitExpirePercent;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            statistiques = (Statistiques) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiStatistiques");
               dashBoard=statistiques.getDashBoard();
               initDataStatistique();
            chargerLineChartController();              
        chargerPieChartController();
        } catch (NotBoundException ex) {
            Logger.getLogger(StatistiqueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(StatistiqueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(StatistiqueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void chartController(ActionEvent e) {
        chargerLineChartController();
        chargerPieChartController();

    }

    public void chargerPieChartController() {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                new PieChart.Data("Vimto", 20),
                new PieChart.Data("Pressea Ananas", 50),
                new PieChart.Data("DonSimon", 17),
                new PieChart.Data("Red-Bull", 15),
                new PieChart.Data("Champagne de Fruits", 32)
        );
        piechart.setData(list);
//Evenement sur les elements du piechart
        for (final PieChart.Data data : piechart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    statut.setText(String.valueOf(data.getPieValue()) + "%");
                }
            });
        }
    }

    private void chargerLineChartController() {
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.getData().add(new XYChart.Data<String, Number>("Jan", 20));
        series.getData().add(new XYChart.Data<String, Number>("Fev", 350));
        series.getData().add(new XYChart.Data<String, Number>("Mars", 330.88));
        series.getData().add(new XYChart.Data<String, Number>("Avr", 500));
        series.getData().add(new XYChart.Data<String, Number>("Mai", 470));
        series.getData().add(new XYChart.Data<String, Number>("Juin", 350.98));

        series.setName("Vente de produits en fonction du mois");
        lineChart.getData().add(series);
        for (final XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    statut.setText("X: " + data.getXValue() + "Y: " + String.valueOf(data.getYValue()));
                    Tooltip.install(data.getNode(), new Tooltip("X: " + data.getXValue() + "\nY: " + String.valueOf(data.getYValue())));
                }
            });
        }
    }

    private void initDataStatistique() {
        idBoutique.setText(Integer.toString(dashBoard.get("boutique")));
        idEmploye.setText(Integer.toString(dashBoard.get("employe")));
        idFournisseur.setText(Integer.toString(dashBoard.get("fournisseur")));
        idClient.setText(Integer.toString(dashBoard.get("client")));
        
        int totalProduit =dashBoard.get("produitEnStock")+dashBoard.get("produitExpire")+dashBoard.get("produitVendu");
        
        idProduitEnStock.setText("Produits en Stock ("+Integer.toString(dashBoard.get("produitEnStock"))+")");
        idProduitVendu.setText("Produits Vendus ("+Integer.toString(dashBoard.get("produitVendu"))+")");
        idProduitExpire.setText("Produits Expires ("+Integer.toString(dashBoard.get("produitExpire"))+")");
             
        idProduitEnStockPercent.setText(Integer.toString(100*dashBoard.get("produitEnStock")/totalProduit)+ "%");
        idProduitVenduPercent.setText(Integer.toString(100*dashBoard.get("produitVendu")/totalProduit)+ "%");
        idProduitExpirePercent.setText(Integer.toString(100*dashBoard.get("produitExpire")/totalProduit)+ "%");
        
    }

}
