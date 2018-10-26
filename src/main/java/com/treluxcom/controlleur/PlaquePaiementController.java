package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTextField;
import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Panier;
import com.treluxcom.metier.Panierclient;
import com.treluxcom.metier.Produit;
import com.treluxcom.service.IFamilleHome;
import com.treluxcom.service.IPanierHome;
import com.treluxcom.service.IPaniercaissierHome;
import com.treluxcom.service.IPanierclientHome;
import com.treluxcom.service.IProduitHome;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.WindowManager;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PlaquePaiementController implements Initializable {

    @FXML
    private ToggleGroup modePaiement;
    @FXML
    private JFXTextField adresseLivraison;
    @FXML
    private Label prixTotal;

    public void setMain(PlaquesMesPaniersController main) {
        this.main = main;
    }

    public void setHashPanier(HashMap<Famille, Double> hashPanier) {
        this.hashPanier = hashPanier;
    }

    public void setHashPrice(HashMap<Famille, Double> hashPrice) {
        this.hashPrice = hashPrice;
    }

    PlaquesMesPaniersController main;

    HashMap<Famille, Double> hashPanier = new HashMap<Famille, Double>();

    HashMap<Famille, Double> hashPrice = new HashMap<Famille, Double>();

    Double montantTotal;

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    class InitThread extends Thread {
        @Override
        public void run() {
            try {
                try {
                    Thread.currentThread().sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(() -> {
                    BigDecimal bd= new BigDecimal(montantTotal);
                    prixTotal.setText((bd) + " FCFA");
                    System.out.println(hashPanier.size());
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

    }

    IPanierclientHome panclihome;
    IProduitHome prodHome;
    ApplicationContext context;
    Panier panier ;
    Panierclient panierclient;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            new InitThread().start();
            context = new ClassPathXmlApplicationContext("spring.xml");
            panier= context.getBean("panier", Panier.class);
            panierclient = context.getBean("panierclient", Panierclient.class);

            IProduitHome prodHome = (IProduitHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiProduitHome");
            IPanierHome panhome = (IPanierHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPanierHome");
            IPanierclientHome panclihome = (IPanierclientHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPanierclientHome");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
List<Produit> prods;
    @FXML
    private void btnAcheter(ActionEvent event) {
        prods = new ArrayList<Produit>();
        try {
            if(adresseLivraison.getText().isEmpty()){
            Notification.notificationSuccess("Avertissement", "Veuillez preciser l'adresse de livraison");
            }
            
            else {
                System.out.println(main.getMain().getPersonne().getClient().getCodepersonne());
                
                for (Map.Entry<Famille, Double> entry : hashPanier.entrySet()) {
                    Famille key = entry.getKey();
                    Double value = entry.getValue();
                    try {
                        prods.addAll(prodHome.retirerProduit(key, hashPanier.get(key).intValue()));
                    } catch (Exception ex) {
                    }

                    System.out.println(prods.size());
                }
                try {
                    Set produits = new HashSet(prods);
                    
                    panier.setCodepanier(GenerateCode.clefUTC("PAN"));
                    panier.setDatepanier(new Date());
                    panier.setPrix(new BigDecimal(prixTotal.getText()));
                    panier.setProduits(produits);
                    
                    panierclient.setClient(main.getMain().getPersonne().getClient());
                    
                    panierclient.setPanier(panier);
                    panierclient.setAdresselivraison(adresseLivraison.getText());
                    panierclient.setTypepaiement(modePaiement.toString());
                    panierclient.setBonlivraison("chemin/" + panier.getCodepanier() + "/.pdf");
                    panclihome.persist(panierclient);
                    
                } catch (Exception exception) {
                }
                try {
                    Notification.notificationSuccess("Achat", "Votre achat est en cours de Livraison");
                    main.getMain().updatePanier();
                    WindowManager.closeWindow(event);
                } catch (Exception e) {
                }
            }
            
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @FXML
    private void btnExit(MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

}
