package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTabPane;
import com.treluxcom.metier.Commande;
import com.treluxcom.metier.Devis;
import com.treluxcom.metier.Fournisseur;
import com.treluxcom.metier.Lignecommande;
import com.treluxcom.metier.Lignedevis;
import com.treluxcom.metier.Personne;
import com.treluxcom.service.ICommandeHome;
import com.treluxcom.utilitaire.Reseau;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainFournisseurController implements Initializable {

    @FXML
    private JFXTabPane tabpane;
    @FXML
    private VBox vboxPrev;
    @FXML
    private VBox vboxCom;

    public Personne personne;

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }
    ApplicationContext context;
    Devis devis;
    List<Lignecommande> lignecom;
    Lignedevis lignedevis;
    List<Commande> listcom;
    ICommandeHome ch;
    int pos=0;
    Fournisseur f;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        context = new ClassPathXmlApplicationContext("spring.xml");
        devis = context.getBean("devis", Devis.class);
        f = context.getBean("fournisseur", Fournisseur.class);
        try {
            ch = (ICommandeHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiCommandeHome");
        } catch (Exception ex) {
            Logger.getLogger(MainFournisseurController.class.getName()).log(Level.SEVERE, null, ex);
        }
        new InitThread().start();
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
                    f = personne.getFournisseur();
                    Iterator coms = null;
                    vboxPrev.getChildren().clear();
                    try {
                        listcom = new ArrayList(f.getCommandes());
                        coms = listcom.iterator();
                        while (coms.hasNext()) {
                            Commande com = (Commande) coms.next();
                            if (com.getEtat() == 0) {
                                Pane newPane = ((Pane) FXMLLoader.load(getClass().getResource("/fxml/PrevCommande.fxml")));
                                ((Label) newPane.getChildren().get(0)).setText(com.getDatecommande().toString());
                                ((Label) newPane.getChildren().get(1)).setText(("Non traité"));

                                newPane.setId(Integer.toString(listcom.indexOf(com)));
                                newPane.setOnMouseClicked((e) -> {
                                    pos = Integer.parseInt(newPane.getId());
                                    try {
                                        System.out.println(newPane.getId());
                                        vboxCom.getChildren().clear();
                                        lignecom = new ArrayList(listcom.get(Integer.parseInt(newPane.getId())).getLignecommandes());
                                        Iterator ls = lignecom.iterator();
                                        while (ls.hasNext()) {
                                            AnchorPane newAnc = ((AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/DetailsCommande.fxml")));
                                            Lignecommande lc = (Lignecommande) ls.next();
                                            ((TextField) newAnc.getChildren().get(0)).setText(lc.getFamille().getNom());
                                            ((TextField) newAnc.getChildren().get(1)).setText(lc.getFamille().getFormat().toString());
                                            ((TextField) newAnc.getChildren().get(2)).setText(lc.getConditionnement().toString());
                                            ((TextField) newAnc.getChildren().get(3)).setText(lc.getQuantite().toString());
                                            ((TextField) newAnc.getChildren().get(4)).setText(lc.getPrixunitaire().toString());
                                            ((TextField) newAnc.getChildren().get(5)).setText(lc.getPrixtotal().toString());

                                            ((Label) newAnc.getChildren().get(6)).setText((lc.getTva()) ? "18%" : "Sans TVA");
                                            ((Label) newAnc.getChildren().get(7)).setText((lc.getFamille().getSucre()) ? "- Sucrée" : "- Sans sucre");
                                            ((Label) newAnc.getChildren().get(8)).setText((lc.getFamille().getMineral()) ? "- Minéral" : "- Non Minéral");
                                            ((Label) newAnc.getChildren().get(9)).setText((lc.getFamille().getNaturel()) ? "- Naturel" : "- Non Naturel");
                                            ((Label) newAnc.getChildren().get(10)).setText((lc.getFamille().getGazeuse()) ? "- Gazeuse" : "- Non Gazeuse");
                                            ((Label) newAnc.getChildren().get(11)).setText((lc.getFamille().getAlcoolise()) ? "- Alcoolisée" : "- Non Alcoolisée");
                                            ((Label) newAnc.getChildren().get(12)).setText("- Degré d'alcool : " + lc.getFamille().getDegresalcool().toString());
                                            ((Label) newAnc.getChildren().get(13)).setText("- " + lc.getFamille().getSaveur());
                                            ((Label) newAnc.getChildren().get(14)).setText(com.getGerant().getEmploye().getPersonne().getPrenom() + " " + com.getGerant().getEmploye().getPersonne().getNom());
                                            vboxCom.getChildren().add(newAnc);
                                        }
                                        // 
                                    } catch (Exception excep) {
                                        excep.printStackTrace();
                                    }
                                });
                                vboxPrev.getChildren().add(newPane);
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    chargerPrev();
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

    }

    @FXML
    public void btncharger(ActionEvent e) {
        chargerPrev();
    }

    public void removeCommande(Commande com) {
        vboxPrev.getChildren().remove(vboxPrev.getChildren().get(pos));
    }

    public void chargerPrev() {
        Iterator coms = null;
        vboxPrev.getChildren().clear();
        try {
            listcom = new ArrayList(ch.commandeList(f.getCodepersonne()));
            coms = listcom.iterator();
            while (coms.hasNext()) {
                Commande com = (Commande) coms.next();
                if (com.getEtat() == 0) {
                    Pane newPane = ((Pane) FXMLLoader.load(getClass().getResource("/fxml/PrevCommande.fxml")));
                    ((Label) newPane.getChildren().get(0)).setText(com.getDatecommande().toString());
                    ((Label) newPane.getChildren().get(1)).setText(("Non traité"));
                    ((Label) newPane.getChildren().get(2)).setText(Integer.toString(com.getLignecommandes().size()));

                    newPane.setId(Integer.toString(listcom.indexOf(com)));
                    newPane.setOnMouseClicked((e) -> {
                        pos = Integer.parseInt(newPane.getId());
                        try {
                            System.out.println(newPane.getId());
                            vboxCom.getChildren().clear();
                            lignecom = new ArrayList(listcom.get(Integer.parseInt(newPane.getId())).getLignecommandes());
                            Iterator ls = lignecom.iterator();
                            while (ls.hasNext()) {
                                AnchorPane newAnc = ((AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/DetailsCommande.fxml")));
                                Lignecommande lc = (Lignecommande) ls.next();
                                ((TextField) newAnc.getChildren().get(0)).setText(lc.getFamille().getNom());
                                ((TextField) newAnc.getChildren().get(1)).setText(lc.getFamille().getFormat().toString());
                                ((TextField) newAnc.getChildren().get(2)).setText(lc.getConditionnement().toString());
                                ((TextField) newAnc.getChildren().get(3)).setText(lc.getQuantite().toString());
                                ((TextField) newAnc.getChildren().get(4)).setText(lc.getPrixunitaire().toString());
                                ((TextField) newAnc.getChildren().get(5)).setText(lc.getPrixtotal().toString());

                                ((Label) newAnc.getChildren().get(6)).setText((lc.getTva()) ? "18%" : "Sans TVA");
                                ((Label) newAnc.getChildren().get(7)).setText((lc.getFamille().getSucre()) ? "- Sucrée" : "- Sans sucre");
                                ((Label) newAnc.getChildren().get(8)).setText((lc.getFamille().getMineral()) ? "- Minéral" : "- Non Minéral");
                                ((Label) newAnc.getChildren().get(9)).setText((lc.getFamille().getNaturel()) ? "- Naturel" : "- Non Naturel");
                                ((Label) newAnc.getChildren().get(10)).setText((lc.getFamille().getGazeuse()) ? "- Gazeuse" : "- Non Gazeuse");
                                ((Label) newAnc.getChildren().get(11)).setText((lc.getFamille().getAlcoolise()) ? "- Alcoolisée" : "- Non Alcoolisée");
                                ((Label) newAnc.getChildren().get(12)).setText("- Degré d'alcool : " + lc.getFamille().getDegresalcool().toString());
                                ((Label) newAnc.getChildren().get(13)).setText("- " + lc.getFamille().getSaveur());
                                ((Label) newAnc.getChildren().get(14)).setText(com.getGerant().getEmploye().getPersonne().getPrenom() + " " + com.getGerant().getEmploye().getPersonne().getNom());
                                vboxCom.getChildren().add(newAnc);
                            }
                            // 
                        } catch (Exception excep) {
                            excep.printStackTrace();
                        }
                    });
                    vboxPrev.getChildren().add(newPane);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void btnFaireDevis(ActionEvent event) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fxml/Devis.fxml"));
            Parent root = (Parent) chargeur.load();
            Scene scene = new Scene(root);
            DevisController devis = chargeur.getController();
            devis.setCom(listcom.get(pos));
            devis.setVboxDevInit(vboxCom);
            devis.setMain(this);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
