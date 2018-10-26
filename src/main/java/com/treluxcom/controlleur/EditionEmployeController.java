/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.controlleur;

import com.treluxcom.metier.Administrateur;
import com.treluxcom.metier.Boutique;
import com.treluxcom.metier.Caissier;
import com.treluxcom.metier.Employe;
import com.treluxcom.metier.Gerant;
import com.treluxcom.metier.Livreur;
import com.treluxcom.metier.Personne;
import com.treluxcom.service.IAdministrateurHome;
import com.treluxcom.service.IBoutiqueHome;
import com.treluxcom.service.ICaissierHome;
import com.treluxcom.service.IGerantHome;
import com.treluxcom.service.ILivreurHome;
import com.treluxcom.service.IPersonneHome;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.SimpleBoutiqueCell;
import com.treluxcom.utilitaire.WindowManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EditionEmployeController implements Initializable {

    @FXML
    private TextField prenom;
    @FXML
    private TextField nom;
    @FXML
    private TextField adresse;
    @FXML
    private TextField lieuNaissance;
    @FXML
    private TextField login;
    @FXML
    private TextField telephone;
    @FXML
    private TextField email;
    @FXML
    private RadioButton masc;
    @FXML
    private ToggleGroup sexe;
    @FXML
    private RadioButton femi;
    @FXML
    private DatePicker dateNaissance;
    @FXML
    private TextField nationalite;
    @FXML
    private TextField droit;
    @FXML
    private ComboBox<String> profil;
    @FXML
    private TextField codeAcces;
    @FXML
    private TextField motpass;
    @FXML
    private Label reponse;
    @FXML
    private ComboBox<Boutique> boutique;
    List<Boutique> listBoutique = new ArrayList<>();
    ObservableList<Boutique> obslistBoutique;
    @FXML
    private Label fermerEdition;

    IBoutiqueHome boutiqueHome;

    String sexeString;
    Employe emp;
    Gerant g;
    Caissier c;
    Livreur l;
    IPersonneHome ph;
    IGerantHome gh;
    ICaissierHome ch;
    ILivreurHome lh;
    IBoutiqueHome bout;
    IAdministrateurHome ah;
    ApplicationContext context;
    Personne personne;
    EnregistrerEmployeController control;

    public void setControl(EnregistrerEmployeController control) {
        this.control = control;
    }
    @FXML
    private AnchorPane edition;

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            context = new ClassPathXmlApplicationContext("spring.xml");
            boutiqueHome = (IBoutiqueHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiBoutiqueHome");
            ph = (IPersonneHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPersonneHome");
            ah = (IAdministrateurHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiAdministrateurHome");
            gh = (IGerantHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiGerantHome");
            ch = (ICaissierHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiCaissierHome");
            lh = (ILivreurHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiLivreurHome");
            bout = (IBoutiqueHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiBoutiqueHome");

            g = context.getBean("gerant", Gerant.class);
            c = context.getBean("caissier", Caissier.class);
            l = context.getBean("livreur", Livreur.class);
            emp = context.getBean("employe", Employe.class);
            initCombo();
            new InitThreadEd().start();
        } catch (Exception ex) {
            ex.printStackTrace();
            //Erreur Reseau
        }
    }

    class InitThreadEd extends Thread {

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
                        initData();
                    } catch (Exception ex) {
                    }
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    private void initData() {
        try {
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(0)).setText(personne.getPrenom());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(1)).setText(personne.getNom());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(2)).setText(personne.getAdresse());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(4)).setText(personne.getLieunaiss());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(5)).setText(personne.getEmail());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(6)).setText(personne.getLogin());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(7)).setText(personne.getMotpasse());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(8)).setText(personne.getTelephone());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(9)).setText(personne.getNationalite());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(10)).setText(personne.getDroit());
            ((TextField) ((Pane) edition.getChildren().get(0)).getChildren().get(11)).setText(personne.getCodeaccess().toString());
            sexeString = personne.getSexe();
        } catch (Exception er) {
        }

    }

    private void initCombo() {
        ObservableList<String> value = FXCollections.observableArrayList();
        value.addAll("Gérant", "Caissier", "Livreur");
        profil.setItems(value);
        profil.getSelectionModel().select(0);
        try {
            listBoutique = boutiqueHome.boutiqueList();

        } catch (RemoteException ex) {
            Logger.getLogger(EditionEmployeController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        boutique.setButtonCell(new SimpleBoutiqueCell());

        obslistBoutique = FXCollections.observableList(listBoutique);
        boutique.setItems(obslistBoutique);
        boutique.setCellFactory(listView -> new SimpleBoutiqueCell());
        boutique.getSelectionModel().select(0);
    }

    @FXML
    private void btnEnregistrer(ActionEvent event) {
        try {
            if (prenom.getText().isEmpty()) {
                reponse.setText("Champ 'Prenom' obligatoire!");

            } else if (nom.getText().isEmpty()) {
                reponse.setText("Champ 'Nom' obligatoire!");

            } else if (adresse.getText().isEmpty()) {
                reponse.setText("Champ 'Adresse' obligatoire!");

            } else if (dateNaissance.getValue() == null) {
                reponse.setText("Champ 'Date de naissance' obligatoire!");

            } else if (email.getText().isEmpty()) {
                reponse.setText("Champ 'Email' obligatoire!");

            } else if (login.getText().isEmpty()) {
                reponse.setText("Champ 'Login' obligatoire!");

            } else if (droit.getText().isEmpty()) {
                reponse.setText("Champ 'Droit' obligatoire!");

            } else if (telephone.getText().isEmpty()) {
                reponse.setText("Champ 'telephone' obligatoire!");
            } else if (codeAcces.getText().isEmpty()) {
                reponse.setText("Champ 'Code d'acces obligatoire!");

            } else if (motpass.getText().isEmpty()) {
                reponse.setText("Champ 'Mot de passe' obligatoire!");

            } else {
                if (Notification.confirmDialog("Veuillez confirmer les modifications sur les informations de  l'employé " + prenom.getText() + " " + nom.getText(), "Enregistrement")) {
                    try {
                        personne.setDatenaiss(dateNaissance.getValue().toString());
                        personne.setLieunaiss(lieuNaissance.getText());

                        personne.setProfil("employe");
                        personne.setSession(false);
                        personne.setBloque(0);
                        personne.setCodeaccess(Integer.parseInt((codeAcces.getText() == null) ? codeAcces.getText() : "0"));

                        personne.setPrenom(prenom.getText());
                        personne.setNom(nom.getText());
                        personne.setEmail(email.getText());
                        personne.setLogin(login.getText());
                        personne.setAdresse(adresse.getText());
                        personne.setTelephone(telephone.getText());
                        personne.setSexe(sexeString);
                        personne.setNationalite(nationalite.getText());
                        personne.setMotpasse(motpass.getText());
                        personne.setDroit(droit.getText());

                        emp.setPersonne(personne);
                        emp.setBoutique(boutique.getSelectionModel().getSelectedItem());
                        emp.setFonction(profil.getSelectionModel().getSelectedItem());

                        if (profil.getSelectionModel().getSelectedItem().equals("Gérant")) {
                            g.setEmploye(emp);
                           gh.deleteById(personne.getCodepersonne());
                            gh.persist(g);
                        } else if (profil.getSelectionModel().getSelectedItem().equals("Caissier")) {
                            c.setEmploye(emp);
                             ch.deleteById(personne.getCodepersonne());
                            ch.persist(c);
                        } else if (profil.getSelectionModel().getSelectedItem().equals("Livreur")) {
                            l.setEmploye(emp);
                           lh.deleteById(personne.getCodepersonne());
                            lh.persist(l);                          
                       }
                        control.replace(personne);
                         Notification.notificationSuccess("Modification", "Les informations de l'employe ont été bien enregistrées");
                         WindowManager.closeWindow(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void textValidator(KeyEvent event) {
    }

    @FXML
    private void btnradioSexe(ActionEvent event) {
        if (masc.isSelected()) {
            sexeString = "m";
        }
        if (femi.isSelected()) {
            sexeString = "f";
        }
    }

    @FXML
    private void btnAnnuler(ActionEvent event) {
    }

}
