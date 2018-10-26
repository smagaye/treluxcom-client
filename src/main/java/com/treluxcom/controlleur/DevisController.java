package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXButton;
import com.treluxcom.metier.Commande;
import com.treluxcom.metier.Devis;
import com.treluxcom.metier.Lignecommande;
import com.treluxcom.metier.Lignedevis;
import com.treluxcom.metier.LignedevisId;
import com.treluxcom.service.ICommandeHome;
import com.treluxcom.service.IDevisHome;
import com.treluxcom.service.ILignedevisHome;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.WindowManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DevisController implements Initializable {

    @FXML
    private VBox vboxDevis;

    public void setCom(Commande com) {
        this.com = com;
    }

    private Commande com;
    private ICommandeHome comhome;
    private IDevisHome devishome;
    private ILignedevisHome lignedevishome;
    private ApplicationContext context;
    private Devis devis;
    @FXML
    private VBox vboxDevInit;
    private MainFournisseurController main;

    public void setMain(MainFournisseurController main) {
        this.main = main;
    }

    public void setVboxDevInit(VBox vboxDevInit) {
        this.vboxDevInit = vboxDevInit;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        context = new ClassPathXmlApplicationContext("spring.xml");
        try {
            devis = context.getBean("devis", Devis.class);
            lignedevishome = (ILignedevisHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiLignedevisHome");
            devishome = (IDevisHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiDevisHome");
            comhome = (ICommandeHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiCommandeHome");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            new InitThread().start();
        } catch (Exception e) {
        }

    }

    @FXML
    private void btnDecliner(ActionEvent ev) {
        try {
            if (Notification.confirmDialog("Voulez-vous vraiment décliner de cette demande ?", "Décliner demande de dévis")) {
                comhome.updateEtat(com, 2);
                ((Node) ev.getSource()).getScene().getWindow().hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnReporter(ActionEvent event) {
        try {
            if (Notification.confirmDialog("Voulez-vous vraiment placer cette demande en dernière position ?", "Quitter")) {
                WindowManager.closeWindow(event);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void btnQuitter(ActionEvent ev) {
        try {
            if (Notification.confirmDialog("Voulez-vous vraiment quitter  ?", "Quitter")) {
                WindowManager.closeWindow(ev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class InitThread extends Thread {

        @Override
        public void run() {
            try {
                try {
                    Thread.currentThread().sleep(16);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Platform.runLater(() -> {
                    try {
                        editerDevis(com);
                    } catch (IOException ex) {
                        Logger.getLogger(DevisController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    public void editerDevis(Commande commande) throws IOException {
        List<Lignedevis> tabLigneDevis = FXCollections.observableArrayList();
        try {

            devis.setCodedevis(GenerateCode.clefUTC("DEV"));
            devis.setCommande(com);
            devis.setFournisseur(com.getFournisseur());

            Iterator lignecoms = commande.getLignecommandes().iterator();
            while (lignecoms.hasNext()) {
                Lignecommande lc = (Lignecommande) lignecoms.next();
                AnchorPane newAnchor = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueDevis.fxml"));
                ((TextField) (newAnchor.getChildren().get(0))).setText(lc.getFamille().getFormat());
                ((TextField) (newAnchor.getChildren().get(1))).setText(lc.getQuantite().toString());

                ((TextField) (newAnchor.getChildren().get(1))).textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d{0,7}([\\.]\\d{0})?")) {
                            ((TextField) (newAnchor.getChildren().get(1))).setText(oldValue);
                        }
                    }
                });

                ((TextField) (newAnchor.getChildren().get(3))).textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d{0,7}([\\.]\\d{2})?")) {
                            ((TextField) (newAnchor.getChildren().get(3))).setText(oldValue);
                        }
                    }
                });

                ((TextField) (newAnchor.getChildren().get(2))).setText(lc.getConditionnement().toString());
                ((TextField) (newAnchor.getChildren().get(3))).setText(lc.getPrixunitaire().toString());
                ((CheckBox) (newAnchor.getChildren().get(4))).setSelected(lc.getTva());
                ((TextField) (newAnchor.getChildren().get(5))).setText(lc.getPrixtotal().toString());
                ((Label) (newAnchor.getChildren().get(6))).setText(lc.getFamille().getNom());

                //Generation de la valeur du champ prix total
                ((TextField) (newAnchor.getChildren().get(3))).setOnKeyReleased((er) -> {
                    if (((TextField) (newAnchor.getChildren().get(3))).getText().isEmpty()) {
                        ((TextField) (newAnchor.getChildren().get(3))).setText("0");
                        ((TextField) (newAnchor.getChildren().get(5))).setText("0");
                    } else if (((TextField) (newAnchor.getChildren().get(1))).getText().isEmpty()) {
                        ((TextField) (newAnchor.getChildren().get(1))).setText("0");
                        ((TextField) (newAnchor.getChildren().get(5))).setText("0");
                    } else {
                      
                        Double ptnum = 0.0;
                        ptnum = Double.parseDouble(((TextField) (newAnchor.getChildren().get(2))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(3))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(1))).getText());
                        ((TextField) (newAnchor.getChildren().get(5))).setText(ptnum.toString());
                    }

                });

                ((TextField) (newAnchor.getChildren().get(1))).setOnKeyReleased((er) -> {
                    if (((TextField) (newAnchor.getChildren().get(3))).getText().isEmpty()) {
                        ((TextField) (newAnchor.getChildren().get(3))).setText("0");
                        ((TextField) (newAnchor.getChildren().get(5))).setText("0");
                    } else if (((TextField) (newAnchor.getChildren().get(1))).getText().isEmpty()) {
                        ((TextField) (newAnchor.getChildren().get(1))).setText("0");
                        ((TextField) (newAnchor.getChildren().get(5))).setText("0");
                    } else {
                        Double ptnum = 0.0;
                        ptnum = Double.parseDouble(((TextField) (newAnchor.getChildren().get(2))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(3))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(1))).getText());
                        ((TextField) (newAnchor.getChildren().get(5))).setText(ptnum.toString());
                    }
                });

                ((CheckBox) (newAnchor.getChildren().get(4))).setOnAction((err) -> {
                    if (((CheckBox) (newAnchor.getChildren().get(4))).isSelected()) {
                        if (((TextField) (newAnchor.getChildren().get(3))).getText().isEmpty()) {
                            ((TextField) (newAnchor.getChildren().get(3))).setText("0");
                            ((TextField) (newAnchor.getChildren().get(5))).setText("0");
                        } else if (((TextField) (newAnchor.getChildren().get(1))).getText().isEmpty()) {
                            ((TextField) (newAnchor.getChildren().get(1))).setText("0");
                            ((TextField) (newAnchor.getChildren().get(5))).setText("0");
                        } else {
                            Double ptnum = 0.0;
                            ptnum = 1.18 * Double.parseDouble(((TextField) (newAnchor.getChildren().get(2))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(3))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(1))).getText());
                            ((TextField) (newAnchor.getChildren().get(5))).setText(ptnum.toString());
                        }
                    } else {
                     
                        Double ptnum = 0.0;
                        ptnum = Double.parseDouble(((TextField) (newAnchor.getChildren().get(2))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(3))).getText()) * Double.parseDouble(((TextField) (newAnchor.getChildren().get(1))).getText());
                        ((TextField) (newAnchor.getChildren().get(5))).setText(ptnum.toString());
                    }
                });
                //Fin Generation prix total

                ((JFXButton) (newAnchor.getChildren().get(7))).setOnAction((e) -> {
                    Lignedevis lignedevis = new Lignedevis();
                    LignedevisId lignedevisid = new LignedevisId();
                    try {
                      
                        lignedevisid.setCodedevis(devis.getCodedevis());
                        lignedevisid.setCodefamille(lc.getFamille().getCodefamille());
                        lignedevis.setFamille(lc.getFamille());
                        lignedevis.setConditionnement(new BigDecimal(0));
                        lignedevis.setQuantite(0);
                        lignedevis.setPrixunitaire(new BigDecimal(((TextField) (newAnchor.getChildren().get(3))).getText()));
                        lignedevis.setTva(Boolean.FALSE);
                        lignedevis.setPrixtotal(BigDecimal.ZERO);
                        lignedevis.setDevis(devis);
                        lignedevis.setFamille(lc.getFamille());
                        lignedevis.setId(lignedevisid);

                        tabLigneDevis.add(lignedevis);
                        vboxDevis.getChildren().remove(newAnchor);
                        if (vboxDevis.getChildren().size() <= 0) {
                            main.chargerPrev();
                            WindowManager.closeWindow(vboxDevis.getParent());
                            if (Notification.confirmDialog("Voulez-envoyer le devis au Gérant de la boutique " + com.getGerant().getEmploye().getBoutique().getNom() + " ?", "Devis")) {
                                vboxDevis.getChildren().remove(newAnchor);
                                insererDevis(tabLigneDevis);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();   
                    }
                });

                ((JFXButton) (newAnchor.getChildren().get(8))).setOnAction((e) -> {
                    try {
                        Lignedevis lignedevis = new Lignedevis();
                        LignedevisId lignedevisid = new LignedevisId();
                        
                        lignedevisid.setCodedevis(devis.getCodedevis());
                        lignedevisid.setCodefamille(lc.getFamille().getCodefamille());
                        lignedevis.setFamille(lc.getFamille());
                        lignedevis.setPrixunitaire(new BigDecimal(((TextField) (newAnchor.getChildren().get(3))).getText()));
                        lignedevis.setPrixtotal(new BigDecimal(((TextField) (newAnchor.getChildren().get(5))).getText()));
                        lignedevis.setConditionnement(new BigDecimal(((TextField) (newAnchor.getChildren().get(2))).getText()));
                        lignedevis.setQuantite(Integer.parseInt(((TextField) (newAnchor.getChildren().get(1))).getText()));
                        lignedevis.setTva(((CheckBox) (newAnchor.getChildren().get(4))).isSelected());
                        lignedevis.setDevis(devis);
                        lignedevis.setFamille(lc.getFamille());
                        lignedevis.setId(lignedevisid);

                        tabLigneDevis.add(lignedevis);
                        vboxDevis.getChildren().remove(newAnchor);
                        if (vboxDevis.getChildren().size() <= 0) {
                             main.chargerPrev();
                            WindowManager.closeWindow(vboxDevis.getParent());
                            if (Notification.confirmDialog("Voulez-envoyer le devis au Gérant de la boutique " + com.getGerant().getEmploye().getBoutique().getNom() + " ?", "Devis")) {
                                vboxDevis.getChildren().remove(newAnchor);
                            }
                            insererDevis(tabLigneDevis);
                        }

                    } catch (NumberFormatException | RemoteException ex) {
                        ex.printStackTrace();                    
                    }
                });

                vboxDevis.getChildren().add(newAnchor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insererDevis(List<Lignedevis> table) throws RemoteException {
        try {
            devishome.persist(devis);
        } catch (RemoteException ex) {
            Logger.getLogger(DevisController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator lds = table.iterator();
        while (lds.hasNext()) {
            try {
                Lignedevis ld = (Lignedevis) lds.next();
                lignedevishome.persist(ld);
                System.out.println("insert" + ld.getDevis().getCodedevis() + " " + ld.getFamille().getCodefamille());
            } catch (Exception ex) {
                Logger.getLogger(DevisController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        comhome.updateEtat(com, 3);
        vboxDevInit.getChildren().clear();
    }
}
