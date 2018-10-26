package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.JFXFillTransition;
import com.treluxcom.metier.Commande;
import com.treluxcom.metier.Devis;
import com.treluxcom.metier.Fournisseur;
import com.treluxcom.metier.Gerant;
import com.treluxcom.metier.Lignedevis;
import com.treluxcom.metier.Personne;
import com.treluxcom.metier.Produit;
import com.treluxcom.metier.Stock;
import com.treluxcom.service.IBoutiqueHome;
import com.treluxcom.service.ICommandeHome;
import com.treluxcom.service.IFournisseurHome;
import com.treluxcom.service.IGerantHome;
import com.treluxcom.service.IStockHome;
import com.treluxcom.service.Statistiques;
import com.treluxcom.utilitaire.DevisPDF;
import com.treluxcom.utilitaire.EmailManager;
import com.treluxcom.utilitaire.FileManager;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AdministrateurController implements Initializable {

    @FXML
    private AnchorPane parent;
    @FXML
    private StackPane stackPanePrincipal;
    @FXML
    private JFXTabPane tabpane3;
    @FXML
    private JFXTabPane tabpane1;
    @FXML
    private JFXTabPane tabpane2;
    @FXML
    private JFXTabPane tabpane4;
    @FXML
    private JFXTabPane tabpane5;
    private JFXTabPane tabpane6;
    @FXML
    private AnchorPane anchorpaneLateral;
    @FXML
    private VBox menuLateral;
    @FXML
    private StackPane stackpaneAlert;
    @FXML
    private AnchorPane anchorpanePasserCommande;
    @FXML
    private ScrollPane scrollPaneFournisseur;
    @FXML
    private VBox vboxFournisseur;
    @FXML
    private ToggleGroup etat;
    @FXML
    private Label statToutesCommandes;
    @FXML
    private Label statCommandeEncours;
    @FXML
    private Label statCommandesValidees;
    @FXML
    private Label statCommandesAnnulees;
    @FXML
    private JFXRadioButton radiovalide;
    @FXML
    private JFXRadioButton radioannule;
    @FXML
    private JFXRadioButton radioencours;
    @FXML
    private JFXRadioButton radiotout;
    @FXML
    private VBox vBoxPrevCommande;
    @FXML
    private Label statSansReponse;
    @FXML
    private JFXRadioButton radiosansreponse;
    @FXML
    private VBox vBoxStock = new VBox();
    @FXML
    private ToggleGroup etatStock;
    @FXML
    private JFXRadioButton stockEncours;
    @FXML
    private JFXRadioButton stockEpuise;
    @FXML
    private JFXRadioButton stockPublie;

    public void setAdmin(Personne admin) {
        this.personne = admin;
    }

    public Personne getAdmin() {
        return this.personne;
    }

    ApplicationContext context;
    List<Fournisseur> listFournisseur;
    IFournisseurHome fourHome;
    IBoutiqueHome boutiqueHome;
    ICommandeHome comhome;
    IStockHome stockHome;
    List<Stock> mesStocks;
    Personne personne;
    IGerantHome gh;
    Gerant gerant;
    Statistiques st;
    HashMap<String, List<Commande>> comStat = new HashMap<String, List<Commande>>();

    @FXML
    private void menuOnMouseEntered(MouseEvent event) {
    }

    @FXML
    private void btnRadioEtat(ActionEvent event) throws IOException {
        try {
            actualiserStat();
            vBoxPrevCommande.getChildren().clear();
            if (radiotout.isSelected()) {
                afficherComtout();
            }
            if (radiovalide.isSelected()) {
                afficherComValide();
            }
            if (radioencours.isSelected()) {
                afficherComencours();
            }
            if (radioannule.isSelected()) {
                afficherComannule();
            }
            if (radiosansreponse.isSelected()) {
                afficherComSansReponse();
            }
        } catch (Exception exception) {
        }
    }

    private void afficherComValide() throws IOException {
        try {
            comStat = st.commande(personne.getEmploye().getBoutique());
            Double percent;
            for (Commande commande : comStat.get("valide")) {
                HashMap<String, Integer> lignecomStat = new HashMap<String, Integer>();
                lignecomStat = st.statProduit(commande);
                percent = (Double.parseDouble(lignecomStat.get("nombreProduitdispo").toString()) * 100.0) / Double.parseDouble(lignecomStat.get("nombreProduit").toString());
                percent = BigDecimal.valueOf(percent)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                AnchorPane comvalidePane = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueCommandeValide.fxml"));
                ((Label) (comvalidePane.getChildren().get(0))).setText(commande.getDatecommande().toString());
                ((Label) (comvalidePane.getChildren().get(1))).setText(commande.getGerant().getEmploye().getPersonne().getPrenom() + " " + commande.getGerant().getEmploye().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(2))).setText(commande.getFournisseur().getPersonne().getPrenom() + " " + commande.getFournisseur().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(3))).setText(lignecomStat.get("nombreProduit").toString());
                ((Label) (comvalidePane.getChildren().get(4))).setText(lignecomStat.get("nombreProduitdispo").toString());
                ((Label) (comvalidePane.getChildren().get(5))).setText(percent.toString() + " %");
                ((JFXButton) (comvalidePane.getChildren().get(6))).setOnAction((e) -> {
                    List<Devis> dh = new ArrayList(commande.getDevises());
                    List<Lignedevis> ld = new ArrayList(dh.get(0).getLignedevises());
                    DevisPDF.imprimerDevis(ld);
                });

                vBoxPrevCommande.getChildren().add(comvalidePane);
            }
        } catch (Exception exception) {
        }
    }

    private void afficherComSansReponse() throws RemoteException, IOException {
        try {
            comStat = st.commande(personne.getEmploye().getBoutique());
            Double percent;
            for (Commande commande : comStat.get("sansconfirmation")) {
                HashMap<String, Integer> lignecomStat = new HashMap<String, Integer>();
                lignecomStat = st.statProduit(commande);
                percent = Double.parseDouble(lignecomStat.get("nombreProduitdispo").toString()) * 100 / Double.parseDouble(lignecomStat.get("nombreProduit").toString());
                percent = BigDecimal.valueOf(percent)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                AnchorPane comvalidePane = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueCommandeSansReponse.fxml"));
                ((Label) (comvalidePane.getChildren().get(0))).setText(commande.getDatecommande().toString());
                ((Label) (comvalidePane.getChildren().get(1))).setText(commande.getGerant().getEmploye().getPersonne().getPrenom() + " " + commande.getGerant().getEmploye().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(2))).setText(commande.getFournisseur().getPersonne().getPrenom() + " " + commande.getFournisseur().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(2))).setText(commande.getFournisseur().getPersonne().getPrenom() + " " + commande.getFournisseur().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(3))).setText(lignecomStat.get("nombreProduit").toString());
                ((Label) (comvalidePane.getChildren().get(4))).setText(lignecomStat.get("nombreProduitdispo").toString());
                ((Label) (comvalidePane.getChildren().get(5))).setText(percent.toString() + " %");
                vBoxPrevCommande.getChildren().add(comvalidePane);
            }
        } catch (Exception exception) {
        }
    }

    private void afficherComannule() throws IOException {
        try {
            comStat = st.commande(personne.getEmploye().getBoutique());
            Double percent;
            for (Commande commande : comStat.get("annule")) {
                HashMap<String, Integer> lignecomStat = new HashMap<String, Integer>();
                lignecomStat = st.statProduit(commande);
                percent = Double.parseDouble(lignecomStat.get("nombreProduitdispo").toString()) * 100 / Double.parseDouble(lignecomStat.get("nombreProduit").toString());
                percent = BigDecimal.valueOf(percent)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                AnchorPane comvalidePane = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueCommandeAnnuler.fxml"));
                ((Label) (comvalidePane.getChildren().get(0))).setText(commande.getDatecommande().toString());
                ((Label) (comvalidePane.getChildren().get(1))).setText(commande.getGerant().getEmploye().getPersonne().getPrenom() + " " + commande.getGerant().getEmploye().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(2))).setText(commande.getFournisseur().getPersonne().getPrenom() + " " + commande.getFournisseur().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(3))).setText(lignecomStat.get("nombreProduit").toString());
                ((Label) (comvalidePane.getChildren().get(4))).setText(lignecomStat.get("nombreProduitdispo").toString());
                ((Label) (comvalidePane.getChildren().get(5))).setText(percent.toString() + " %");

                vBoxPrevCommande.getChildren().add(comvalidePane);
            }
        } catch (Exception Exception) {
        }
    }

    private void afficherComencours() throws IOException {
        try {
            comStat = st.commande(personne.getEmploye().getBoutique());
            Double percent;
            for (Commande commande : comStat.get("encours")) {
                HashMap<String, Integer> lignecomStat = new HashMap<String, Integer>();
                lignecomStat = st.statProduit(commande);
                percent = Double.parseDouble(lignecomStat.get("nombreProduitdispo").toString()) * 100 / Double.parseDouble(lignecomStat.get("nombreProduit").toString());
                percent = BigDecimal.valueOf(percent)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                AnchorPane comvalidePane = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueCommandeEncours.fxml"));

                ((Label) (comvalidePane.getChildren().get(0))).setText(commande.getDatecommande().toString());
                ((Label) (comvalidePane.getChildren().get(1))).setText(commande.getGerant().getEmploye().getPersonne().getPrenom() + " " + commande.getGerant().getEmploye().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(2))).setText(commande.getFournisseur().getPersonne().getPrenom() + " " + commande.getFournisseur().getPersonne().getNom());
                ((Label) (comvalidePane.getChildren().get(3))).setText(lignecomStat.get("nombreProduit").toString());
                ((Label) (comvalidePane.getChildren().get(4))).setText(lignecomStat.get("nombreProduitdispo").toString());
                ((JFXButton) (comvalidePane.getChildren().get(5))).setOnAction((e) -> {
                    if (Notification.confirmDialog("Voulez-vous vraiment acheter ces produits ?", "Achat de Produits")) {
                        try {
                            comhome.updateEtat(commande, 1);
                            vBoxPrevCommande.getChildren().remove(comvalidePane);
                            if (EmailManager.send("treluxcomprojet@gmail.com", "Je confirme l'achat des produits", "Achat produit")) {
                                Notification.notificationSuccess("Envoi message", "Message envoye!");
                            } else {
                                Notification.notificationErr("Envoi message", "Message non envoye car \nvous n'etes pas connecte a internet !");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(AdministrateurController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                ((JFXButton) (comvalidePane.getChildren().get(6))).setOnAction((e) -> {
                    if (Notification.confirmDialog("Voulez-vous vraiment décliner la proposition du fournisseur " + commande.getFournisseur().getPersonne().getPrenom() + " " + commande.getFournisseur().getPersonne().getNom() + " ?", "Décliner demande de dévis")) {
                        try {
                            comhome.updateEtat(commande, 2);
                            vBoxPrevCommande.getChildren().remove(comvalidePane);
                        } catch (Exception ex) {
                            Logger.getLogger(AdministrateurController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                comvalidePane.setOnMouseClicked((e) -> {
                    List<Devis> dh = new ArrayList(commande.getDevises());
                    List<Lignedevis> ld = new ArrayList(dh.get(0).getLignedevises());
                    DevisPDF.imprimerDevis(ld);
                });
                vBoxPrevCommande.getChildren().add(comvalidePane);
            }
        } catch (NumberFormatException numberFormatException) {
        } catch (IOException iOException) {
        }
    }

    private void afficherComtout() throws RemoteException, IOException {
        try {
            afficherComValide();
            afficherComSansReponse();
            afficherComencours();
            afficherComannule();
        } catch (Exception exception) {
        }
    }

    @FXML
    private void radioStock(ActionEvent event) throws IOException {
        try {
            mesStocks.clear();
            if (stockEncours.isSelected()) {
                mesStocks = stockHome.stockList();
                afficherToutStock("rgba(255, 255, 255, 0.5)");
            }
            if (stockPublie.isSelected()) {
                mesStocks = stockHome.stockPublieList();
                afficherToutStock("#4caf50");
            }
            if (stockEpuise.isSelected()) {
                List<Stock> tmpStock = stockHome.stockList();
                int comp = mesStocks.size();
                for (int i = 0; i < comp; i++) {
                    if (stockHome.isEmpty(tmpStock.get(i))) {
                        mesStocks.add(tmpStock.get(i));
                    }
                }
                afficherToutStock("orange");
            }

        } catch (Exception ex) {
        }

    }

    private void afficherToutStock(String colorPane) throws IOException {
        try {
            vBoxStock.getChildren().clear();
            int nbreHbox = (mesStocks.size() / 3) + 1;
            int j = 0;
            for (int i = 0; i < nbreHbox; i++) {

                HBox hBox = new HBox();
                hBox.setPrefWidth(200);
                hBox.setPrefHeight(100);
                hBox.setSpacing(10);

                try {
                    for (int k = 0; k < 3; k++) {
                        AnchorPane plaque = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueStock.fxml"));
                        plaque.setBackground(Background.EMPTY);
                        String style = "-fx-background-color:" + colorPane + ";";
                        plaque.setStyle(style);
                        HashMap<String, List<Produit>> stat = new HashMap<String, List<Produit>>();
                        try {
                            stat = st.stock(mesStocks.get(j));

                            ((Label) plaque.getChildren().get(0)).setText(mesStocks.get(j).getCodestock());
                            ((Label) plaque.getChildren().get(1)).setText(mesStocks.get(j).getQuantitetotal().toString());
                            ((JFXProgressBar) plaque.getChildren().get(2)).setProgress(1);

                            ((Label) plaque.getChildren().get(3)).setText(Integer.toString(stat.get("produitEnstock").size()));
                            ((JFXProgressBar) plaque.getChildren().get(4)).setProgress((stat.get("produitEnstock").size()) / (double) (mesStocks.get(j).getQuantitetotal()));

                            ((Label) plaque.getChildren().get(5)).setText(Integer.toString(stat.get("produitVenduEnligne").size() + stat.get("produitVenduenBoutique").size()));
                            ((JFXProgressBar) plaque.getChildren().get(6)).setProgress((stat.get("produitVenduEnligne").size() + stat.get("produitVenduenBoutique").size()) / (double) (mesStocks.get(j).getQuantitetotal()));

                            ((Label) plaque.getChildren().get(7)).setText(Integer.toString(stat.get("produitExpire").size()));
                            ((JFXProgressBar) plaque.getChildren().get(8)).setProgress((stat.get("produitExpire").size()) / (double) (mesStocks.get(j).getQuantitetotal()));

                            Label label = ((Label) (plaque.getChildren().get(0)));
                            String code =mesStocks.get(j).getCodestock();
                            ((MenuItem) label.getContextMenu().getItems().get(1)).setOnAction((e) -> {
                                try {
                                    Desktop.getDesktop().open(new File("./src/main/resources/pdf/qrcode/"+code+".pdf"));
                                } catch (IOException ex) {
                                    Logger.getLogger(AdministrateurController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            });
                            
                            if(!mesStocks.get(j).getPublier()){
                            ((MenuItem) label.getContextMenu().getItems().get(2)).setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    if (Notification.confirmDialog("Voulez-vous vraiment publier ce stock", "Publication")) {
                                        updateStock(code, "true");
                                    }
                                }
                            });
                            } else{
                             ((MenuItem) label.getContextMenu().getItems().get(2)).setText("Annuler Publication");
                             ((MenuItem) label.getContextMenu().getItems().get(2)).setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    Notification.notificationSuccess("3", "Publier");
                                    if (Notification.confirmDialog("Voulez-vous vraiment annuler la publication ce stock", "Publication")) {
                                        updateStock(code,"false");
                                    }
                                }
                            });

                            }
                            hBox.getChildren().add(plaque);
                            j++;
                        } catch (Exception ex) {

                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                vBoxStock.getChildren().add(hBox);

            }
        } catch (Exception e) {
        }
    }

    public void updateStock(String codeStock , String act) {
        try {
            stockHome.update("publier", act, "codestock", codeStock);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                    try {
                        boutiqueHome = (IBoutiqueHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiBoutiqueHome");
                        comStat = st.commande(personne.getEmploye().getBoutique());
                        actualiserStat();
                        afficherComencours();
                    } catch (Exception ex) {
                        Logger.getLogger(AdministrateurController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    private void actualiserStat() {
        try {
            statToutesCommandes.setText(Integer.toString(comStat.get("tout").size()));
            statCommandeEncours.setText(Integer.toString(comStat.get("encours").size()));
            statCommandesValidees.setText(Integer.toString(comStat.get("valide").size()));
            statCommandesAnnulees.setText(Integer.toString(comStat.get("annule").size()));
            statSansReponse.setText(Integer.toString(comStat.get("sansconfirmation").size()));
        } catch (Exception e) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            new InitThread().start();
            context = new ClassPathXmlApplicationContext("spring.xml");
            gerant = context.getBean("gerant", Gerant.class);
            fourHome = (IFournisseurHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiFournisseurHome");
            comhome = (ICommandeHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiCommandeHome");
            gh = (IGerantHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiGerantHome");
            st = (Statistiques) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiStatistiques");
            stockHome = (IStockHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiStockHome");
            mesStocks = stockHome.stockList();
            afficherToutStock("rgba(255, 255, 255, 0.5)");
            listFournisseur();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void listFournisseur() {
        try {
            vboxFournisseur.getChildren().clear();
            listFournisseur = fourHome.fournisseurList();
            Iterator fours = listFournisseur.iterator();
            while (fours.hasNext()) {
                Fournisseur four = (Fournisseur) fours.next();

                Pane newPane = ((Pane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueFournisseur.fxml")));
                ((TextField) newPane.getChildren().get(1)).setText(four.getPersonne().getPrenom() + " " + four.getPersonne().getNom());
                ((TextField) newPane.getChildren().get(2)).setText((four.getPersonne().getSexe().equals("m")) ? "masculin" : "feminin");
                ((TextField) newPane.getChildren().get(3)).setText(four.getPersonne().getAdresse());
                ((TextField) newPane.getChildren().get(4)).setText(four.getPersonne().getTelephone());
                ((TextField) newPane.getChildren().get(5)).setText(four.getPersonne().getEmail());
                ((TextField) newPane.getChildren().get(6)).setText(Integer.toString(four.getCommandes().size()));
                ((TextField) newPane.getChildren().get(7)).setText(Integer.toString(four.getDevises().size()));
                ((TextField) newPane.getChildren().get(8)).setText(four.getPersonne().getLogin());

                JFXButton bouton = ((JFXButton) newPane.getChildren().get(0));
                bouton.setId(Integer.toString(listFournisseur.indexOf(four)));
                bouton.setOnAction((e) -> {
                    try {

                        gerant = personne.getEmploye().getGerant();
                        Stage stage = new Stage();
                        FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fxml/PasserCommande.fxml"));
                        Parent root = (Parent) chargeur.load();
                        Scene scene = new Scene(root);
                        PasserCommandeController passerCommande = chargeur.getController();
                        passerCommande.setFournisseur(listFournisseur.get(Integer.parseInt(bouton.getId())));
                        passerCommande.setGerant(gerant);
                        stage.setScene(scene);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        stage.show();

                    } catch (Exception excep) {
                        excep.printStackTrace();
                    }
                });
                vboxFournisseur.getChildren().add(newPane);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnGestionStockevent(ActionEvent event) {
        try {
            stackPanePrincipal.getChildren().clear();
            stackPanePrincipal.getChildren().add(tabpane1);
            tabpane1.setVisible(true);
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnCommandesEvent(ActionEvent event) {
        try {
            listFournisseur();
            stackPanePrincipal.getChildren().clear();
            stackPanePrincipal.getChildren().add(tabpane2);
            tabpane2.setVisible(true);
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnBoutiqueEvent(ActionEvent event) {
        try {
            stackPanePrincipal.getChildren().clear();
            stackPanePrincipal.getChildren().add(tabpane3);
            tabpane3.setVisible(true);
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnCalendrierServiceEvent(ActionEvent event) {
        try {
            /*   Decommentez cette partie pour voir l'etat d'avancement           
            stackPanePrincipal.getChildren().clear();
            stackPanePrincipal.getChildren().add(tabpane4);
            tabpane4.setVisible(true);
            */
            Notification.notificationSuccess("En construction", "Veuillez consulter le controlleur com.treluxcom.controlleur.AdministrateurController");
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnMessagerie(ActionEvent event) {
        try {
            /*   Decommentez cette partie pour voir l'etat d'avancement         
            stackPanePrincipal.getChildren().clear();
            stackPanePrincipal.getChildren().add(tabpane5);
            tabpane5.setVisible(true);
            */
           Notification.notificationSuccess("En construction", "Veuillez consulter le controlleur com.treluxcom.controlleur.AdministrateurController");
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnStatistiquesEvent(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void declencheurMenuOnMouseEntered(MouseEvent event) {
        try {
            menuLateral.setVisible(true);
            anchorpaneLateral.setVisible(true);

            JFXFillTransition transition = new JFXFillTransition();
            transition.setDelay(Duration.millis(200));
            transition.setRegion(anchorpaneLateral);
            transition.setFromValue(Color.TRANSPARENT);
            transition.setToValue(Color.WHITE);
            transition.play();

            // anchorpaneLateral.setStyle("-fx-background-color: white;");
            anchorpaneLateral.setStyle("-fx-width: 780px;");
        } catch (Exception e) {
        }
    }

    @FXML
    private void menuLateralOnMouseExited(MouseEvent event) {
        try {
            JFXFillTransition transition = new JFXFillTransition();
            transition.setDelay(Duration.millis(200));
            transition.setRegion(anchorpaneLateral);
            transition.setFromValue(Color.WHITE);
            transition.setToValue(Color.TRANSPARENT);
            transition.play();
            transition.setOnFinished((e) -> {
                anchorpaneLateral.setVisible(false);
                menuLateral.setVisible(false);
            });
        } catch (Exception e) {
        }
        //anchorpaneLateral.setStyle("-fx-background-color: transparent;");
    }

}
