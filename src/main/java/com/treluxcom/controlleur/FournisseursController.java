package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.treluxcom.metier.Fournisseur;
import com.treluxcom.metier.Personne;
import com.treluxcom.service.IFournisseurHome;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.hibernate.HibernateException;

public class FournisseursController implements Initializable {

    String sexeString = "m";

    IFournisseurHome fh;

    @FXML
    private TextField prenom;

    @FXML
    private TextField nom;

    @FXML
    private TextField lieuNaissance;

    @FXML
    private TextField login;

    @FXML
    private TextField telephone;

    @FXML
    private TextField adresse;

    @FXML
    private TextField email;

    @FXML
    private RadioButton masc;

    @FXML
    private ToggleGroup sexe;

    @FXML
    private RadioButton fem;

    @FXML
    private DatePicker dateNaissance;

    @FXML
    private TextField nationalite;

    @FXML
    private TextField droit;

    @FXML
    private TextField codeAcces;

    @FXML
    private TextField motpass;

    @FXML
    private JFXTreeTableView<TableFournisseur> tableFournisseur;

    @FXML
    private JFXTextField recherche;
    @FXML
    private Label reponse;
    @FXML
    private ImageView reponseImg;

    @FXML
    void btnAnnuler(ActionEvent event) {
        annuler();
    }

    @FXML
    private void btnEnregistrer(ActionEvent event) {

        try {
            if (prenom.getText().isEmpty()) {
                reponse.setText("Champ 'Prenom' obligatoire!");
                reponseImg.setVisible(true);
            } else if (nom.getText().isEmpty()) {
                reponse.setText("Champ 'Nom' obligatoire!");
                reponseImg.setVisible(true);
            } else if (adresse.getText().isEmpty()) {
                reponse.setText("Champ 'Adresse' obligatoire!");
                reponseImg.setVisible(true);
            } else if (email.getText().isEmpty()) {
                reponse.setText("Champ 'Email' obligatoire!");
                reponseImg.setVisible(true);
            } else if (login.getText().isEmpty()) {
                reponse.setText("Champ 'Login' obligatoire!");
                reponseImg.setVisible(true);
            } else if (telephone.getText().isEmpty()) {
                reponse.setText("Champ 'telephone' obligatoire!");
                reponseImg.setVisible(true);
            } else if (motpass.getText().isEmpty()) {
                reponse.setText("Champ 'Mot de passe' obligatoire!");
                reponseImg.setVisible(true);
            } else {
                if (Notification.confirmDialog("Veuillez confirmer l'ajout du fournisseur " + prenom.getText() + " " + nom.getText(), "Enregistrement")) {
                    try {
                        Personne personne = new Personne();
                        personne.setCodepersonne(GenerateCode.clefUTC("FOU"));
                        Fournisseur fournisseur = new Fournisseur();
                        personne.setDatenaiss(dateNaissance.getValue().toString());
                        personne.setLieunaiss(lieuNaissance.getText());

                        personne.setProfil("fournisseur");
                        personne.setSession(false);
                        personne.setBloque(0);
                        personne.setNombreconnexion(0);
                        personne.setCodeaccess(0);

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
                        fournisseur.setPersonne(personne);
                        fh.persist(fournisseur);
                        tableFournisseurList.add(new TableFournisseur(
                                fournisseur.getPersonne().getCodepersonne(),
                                fournisseur.getPersonne().getNom(),
                                fournisseur.getPersonne().getPrenom(),
                                fournisseur.getPersonne().getLogin(),
                                fournisseur.getPersonne().getEmail(),
                                fournisseur.getPersonne().getTelephone(),
                                fournisseur.getPersonne().getAdresse(),
                                fournisseur.getPersonne().getDatenaiss(),
                                fournisseur.getPersonne().getLieunaiss()
                        )
                        );
                        Notification.notificationSuccess("Insertion", "Le fournisseur a été bien enregistré");
                        annuler();
                    } catch (NullPointerException e) {
                        reponse.setText("Date de naissance Obligatoire!");
                        e.printStackTrace();
                    } catch (HibernateException err) {
                        Notification.notificationErr("Erreur", "Problème avec le Serveur Base de données");
                    } catch (RemoteException ex) {
                        Logger.getLogger(FournisseursController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (NullPointerException err) {
            err.printStackTrace();
        }
    }

    private void annuler() {
        prenom.setText("");
        nom.setText("");
        adresse.setText("");
        masc.setSelected(true);
        dateNaissance.setValue(LocalDate.now());
        lieuNaissance.setText("");
        login.setText("");
        email.setText("");
        motpass.setText("");
        nationalite.setText("");
        telephone.setText("");
        droit.setText("");
        codeAcces.setText("");
        reponse.setText("");
    }

    @FXML
    private void btnradioSexe(ActionEvent event) {
        if (masc.isSelected()) {
            sexeString = "m";
        }
        if (fem.isSelected()) {
            sexeString = "f";
        }
    }

    @FXML
    private void mnEditer(ActionEvent event) {
    }

    @FXML
    private void mnSupprimer(ActionEvent event) {
        TableFournisseur te = tableFournisseur.getSelectionModel().getSelectedItem().getValue();
        if (Notification.confirmDialog("Voulez-vous supprimer le fournisseur " + te.getPrenom() + " " + te.getNom(), "Suppression")) {
            try {
                fh.deleteById(te.codepersonne.get());
                tableFournisseur.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tableFournisseur.getSelectionModel().getSelectedItem());
                Notification.notificationSuccess("Suppression", "Le fournisseur a été supprimé avec succès");
            } catch (Exception err) {
                Notification.notificationErr("Suppression", "Opération Echouée!");
                err.printStackTrace();

            }
        }

    }

    class TableFournisseur extends RecursiveTreeObject<TableFournisseur> {

        StringProperty nom;
        StringProperty codepersonne;
        StringProperty prenom;
        StringProperty adresse;
        StringProperty telephone;
        StringProperty email;
        StringProperty dateNaissance;
        StringProperty lieuNaissance;

        public String getNom() {
            return nom.get();
        }

        public String getCodepersonne() {
            return codepersonne.get();
        }

        public String getPrenom() {
            return prenom.get();
        }

        public String getAdresse() {
            return adresse.get();
        }

        public String getTelephone() {
            return telephone.get();
        }

        public String getEmail() {
            return email.get();
        }

        public String getDateNaissance() {
            return dateNaissance.get();
        }

        public String getLieuNaissance() {
            return lieuNaissance.get();
        }

        public String getProfil() {
            return login.get();
        }
        StringProperty login;

        public TableFournisseur(String codepersonne, String nom, String prenom, String login, String email, String telephone, String adresse, String dateNaissance, String lieuNaissance) {
            this.codepersonne = new SimpleStringProperty(codepersonne);
            this.nom = new SimpleStringProperty(nom);
            this.prenom = new SimpleStringProperty(prenom);
            this.email = new SimpleStringProperty(email);
            this.telephone = new SimpleStringProperty(telephone);
            this.adresse = new SimpleStringProperty(adresse);
            this.dateNaissance = new SimpleStringProperty(dateNaissance);
            this.lieuNaissance = new SimpleStringProperty(lieuNaissance);
            this.login = new SimpleStringProperty(login);

        }
    }
    ObservableList<TableFournisseur> tableFournisseurList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            codeAcces.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,2}([\\.]\\d{0})?")) {
                        codeAcces.setText(oldValue);
                    }
                }
            });
            System.out.println("Page Fournisseur chargée");
            try {
                fh = (IFournisseurHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiFournisseurHome");
            } catch (Exception e) {
            }
            initTableview();

            recherche.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    tableFournisseur.setPredicate(new Predicate<TreeItem<TableFournisseur>>() {
                        @Override
                        public boolean test(TreeItem<TableFournisseur> t) {
                            Boolean flag = t.getValue().nom.getValue().contains(newValue) || t.getValue().prenom.getValue().contains(newValue) || t.getValue().email.getValue().contains(newValue);
                            return flag;
                        }
                    }
                    );
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initTableview() throws RemoteException {
        JFXTreeTableColumn<TableFournisseur, String> codepersonneCol = new JFXTreeTableColumn<>("Code personne");
        codepersonneCol.setPrefWidth(80);

        codepersonneCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().prenom;
            }
        });
        JFXTreeTableColumn<TableFournisseur, String> prenomCol = new JFXTreeTableColumn<>("Prenom");
        prenomCol.setPrefWidth(80);
        prenomCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().prenom;
            }
        });

        JFXTreeTableColumn<TableFournisseur, String> nomcol = new JFXTreeTableColumn<>("Nom");
        nomcol.setPrefWidth(150);
        nomcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().nom;
            }
        });
        JFXTreeTableColumn<TableFournisseur, String> emailcol = new JFXTreeTableColumn<>("E-mail");
        emailcol.setPrefWidth(150);
        emailcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().email;
            }
        });

        JFXTreeTableColumn<TableFournisseur, String> telephonecol = new JFXTreeTableColumn<>("Téléphone");
        telephonecol.setPrefWidth(90);
        telephonecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().telephone;
            }
        });

        JFXTreeTableColumn<TableFournisseur, String> adressecol = new JFXTreeTableColumn<>("Adresse");
        adressecol.setPrefWidth(90);
        adressecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().adresse;
            }
        });

        JFXTreeTableColumn<TableFournisseur, String> dateNaissancecol = new JFXTreeTableColumn<>("Date Naissance");
        dateNaissancecol.setPrefWidth(150);
        dateNaissancecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().dateNaissance;
            }
        });

        JFXTreeTableColumn<TableFournisseur, String> lieunaissancecol = new JFXTreeTableColumn<>("Lieu de naissance");
        lieunaissancecol.setPrefWidth(150);
        lieunaissancecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().lieuNaissance;
            }
        });
        JFXTreeTableColumn<TableFournisseur, String> logincol = new JFXTreeTableColumn<>("Login");
        logincol.setPrefWidth(150);
        logincol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFournisseur, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFournisseur, String> param) {
                return param.getValue().getValue().login;
            }
        });

        //	ObservableList<TableFournisseur> tableFournisseurList =FXCollections.observableArrayList();
        Iterator fourIt = fh.fournisseurList().iterator();
        while (fourIt.hasNext()) {
            Fournisseur fourElm = (Fournisseur) fourIt.next();
            tableFournisseurList.add(new TableFournisseur(
                    fourElm.getPersonne().getCodepersonne(),
                    fourElm.getPersonne().getNom(),
                    fourElm.getPersonne().getPrenom(),
                    fourElm.getPersonne().getLogin(),
                    fourElm.getPersonne().getEmail(),
                    fourElm.getPersonne().getTelephone(),
                    fourElm.getPersonne().getAdresse(),
                    fourElm.getPersonne().getDatenaiss(),
                    fourElm.getPersonne().getLieunaiss()
            )
            );
        }

        final TreeItem<TableFournisseur> root1 = new RecursiveTreeItem<TableFournisseur>(tableFournisseurList, RecursiveTreeObject::getChildren);
        tableFournisseur.getColumns().setAll(prenomCol, nomcol, logincol, emailcol, telephonecol, adressecol, dateNaissancecol, lieunaissancecol);
        tableFournisseur.setRoot(root1);
        tableFournisseur.setShowRoot(false);
    }

}
