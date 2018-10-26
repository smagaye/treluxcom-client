package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
import com.treluxcom.service.IEmployeHome;
import com.treluxcom.service.IGerantHome;
import com.treluxcom.service.ILivreurHome;
import com.treluxcom.service.IPersonneHome;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.SimpleBoutiqueCell;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.HibernateException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EnregistrerEmployeController implements Initializable {

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
    private RadioButton femi;
    @FXML
    private ToggleGroup sexe;
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
    private JFXTextField recherche;

    String sexeString = "m";
    ObservableList<String> value = FXCollections.observableArrayList();
    ApplicationContext context;

    @FXML
    private Label reponse;
    private ImageView reponseImg;
    @FXML
    private JFXTreeTableView<TableEmploye> tableEmploye;

    @FXML
    private ComboBox<Boutique> boutique;
    List<Boutique> listBoutique = new ArrayList<>();
    ObservableList<Boutique> obslistBoutique;
    @FXML
    private AnchorPane anchorpaneEmploye;

    Personne personne;
    Administrateur admin;
    Personne p;
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
    IEmployeHome eph;

    public void chargerEdition() {
        try {
            AnchorPane edition;
            TableEmploye te = tableEmploye.getSelectionModel().getSelectedItem().getValue();
            p = ph.findById(te.codepersonne.get());

            Stage primaryStage = new Stage();
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fxml/EditionEmploye.fxml"));
            Parent root = (Parent) chargeur.load();
            Scene sceneEdit = new Scene(root);
            EditionEmployeController editionEmployeController = chargeur.getController();
            editionEmployeController.setPersonne(p);
            editionEmployeController.setControl(this);

            primaryStage.setTitle("Edition Employe");
            primaryStage.setScene(sceneEdit);
            primaryStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void mnDetail(ActionEvent event) {
    }

    @FXML
    private void mnEditer(ActionEvent event) {
        chargerEdition();
    }

    @FXML
    private void mnSupprimer(ActionEvent event) {
        TableEmploye te = tableEmploye.getSelectionModel().getSelectedItem().getValue();
        if (Notification.confirmDialog("Voulez-vous supprimer l'employé " + te.getPrenom() + " " + te.getNom(), "Suppression")) {
            try {
                if (te.getProfil().equals("Gérant")) {
                    gh.deleteById(te.codepersonne.get());
                } else if (te.getProfil().equals("Caissier")) {
                    ch.deleteById(te.codepersonne.get());
                } else if (te.getProfil().equals("Livreur")) {
                    lh.deleteById(te.codepersonne.get());
                }
                tableEmploye.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tableEmploye.getSelectionModel().getSelectedItem());
                Notification.notificationSuccess("Suppression", "Réussie");
            } catch (HibernateException err) {
                Notification.notificationErr("Suppression", "Echoué");
                err.printStackTrace();
            } catch (RemoteException ex) {
                Logger.getLogger(EnregistrerEmployeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void btnAnnuler(ActionEvent event
    ) {
        annuler();
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
        profil.getSelectionModel().select(0);
        boutique.getSelectionModel().select(0);

    }

    class TableEmploye extends RecursiveTreeObject<TableEmploye> {

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
            return profil.get();
        }
        StringProperty profil;

        public TableEmploye(String codepersonne, String nom, String prenom, String profil, String email, String telephone, String adresse, String dateNaissance, String lieuNaissance) {
            this.codepersonne = new SimpleStringProperty(codepersonne);
            this.nom = new SimpleStringProperty(nom);
            this.prenom = new SimpleStringProperty(prenom);
            this.email = new SimpleStringProperty(email);
            this.telephone = new SimpleStringProperty(telephone);
            this.adresse = new SimpleStringProperty(adresse);
            this.dateNaissance = new SimpleStringProperty(dateNaissance);
            this.lieuNaissance = new SimpleStringProperty(lieuNaissance);
            this.profil = new SimpleStringProperty(profil);

        }
    }
    ObservableList<TableEmploye> tableEmployeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            context = new ClassPathXmlApplicationContext("spring.xml");
            codeAcces.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,2}([\\.]\\d{0})?")) {
                        codeAcces.setText(oldValue);
                    }
                }
            });

            telephone.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,9}([\\+]\\d{0})?")) {
                        telephone.setText(oldValue);
                    }
                }
            });
            ph = (IPersonneHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPersonneHome");
            ah = (IAdministrateurHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiAdministrateurHome");
            gh = (IGerantHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiGerantHome");
            ch = (ICaissierHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiCaissierHome");
            lh = (ILivreurHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiLivreurHome");
            bout = (IBoutiqueHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiBoutiqueHome");
            eph = (IEmployeHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiEmployeHome");
            admin = context.getBean("administrateur", Administrateur.class);
            admin = ah.administrateurList().get(0);

            personne = context.getBean("personne", Personne.class);
            p = context.getBean("personne", Personne.class);
            g = context.getBean("gerant", Gerant.class);
            c = context.getBean("caissier", Caissier.class);
            l = context.getBean("livreur", Livreur.class);
            emp = context.getBean("employe", Employe.class);

            initCombo();
            initTableview();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initTableview() throws RemoteException {
        JFXTreeTableColumn<TableEmploye, String> codepersonneCol = new JFXTreeTableColumn<>("Code personne");
        codepersonneCol.setPrefWidth(80);

        codepersonneCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().codepersonne;
            }
        });
        JFXTreeTableColumn<TableEmploye, String> prenomCol = new JFXTreeTableColumn<>("Prenom");
        prenomCol.setPrefWidth(80);
        prenomCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().prenom;
            }
        });

        JFXTreeTableColumn<TableEmploye, String> nomcol = new JFXTreeTableColumn<>("Nom");
        nomcol.setPrefWidth(150);
        nomcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().nom;
            }
        });
        JFXTreeTableColumn<TableEmploye, String> emailcol = new JFXTreeTableColumn<>("E-mail");
        emailcol.setPrefWidth(150);
        emailcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().email;
            }
        });

        JFXTreeTableColumn<TableEmploye, String> telephonecol = new JFXTreeTableColumn<>("Téléphone");
        telephonecol.setPrefWidth(90);
        telephonecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().telephone;
            }
        });

        JFXTreeTableColumn<TableEmploye, String> adressecol = new JFXTreeTableColumn<>("Adresse");
        adressecol.setPrefWidth(90);
        adressecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().adresse;
            }
        });

        JFXTreeTableColumn<TableEmploye, String> dateNaissancecol = new JFXTreeTableColumn<>("Date Naissance");
        dateNaissancecol.setPrefWidth(150);
        dateNaissancecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().dateNaissance;
            }
        });

        JFXTreeTableColumn<TableEmploye, String> lieunaissancecol = new JFXTreeTableColumn<>("Lieu de naissance");
        lieunaissancecol.setPrefWidth(150);
        lieunaissancecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().lieuNaissance;
            }
        });
        JFXTreeTableColumn<TableEmploye, String> profilcol = new JFXTreeTableColumn<>("Profil");
        profilcol.setPrefWidth(150);
        profilcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableEmploye, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableEmploye, String> param) {
                return param.getValue().getValue().profil;
            }
        });
        //	ObservableList<TableEmploye> tableEmployeList =FXCollections.observableArrayList();
        putContainsTable();

        final TreeItem<TableEmploye> root1 = new RecursiveTreeItem<TableEmploye>(tableEmployeList, RecursiveTreeObject::getChildren);
        tableEmploye.getColumns().setAll(prenomCol, nomcol, profilcol, emailcol, telephonecol, adressecol, dateNaissancecol, lieunaissancecol);
        tableEmploye.setRoot(root1);
        tableEmploye.setShowRoot(false);

        recherche.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tableEmploye.setPredicate(new Predicate<TreeItem<TableEmploye>>() {
                    @Override
                    public boolean test(TreeItem<TableEmploye> t) {
                        Boolean flag = t.getValue().nom.getValue().contains(newValue) || t.getValue().prenom.getValue().contains(newValue) || t.getValue().email.getValue().contains(newValue);
                        return flag;
                    }
                }
                );
            }
        });
    }

    private void initCombo() {

        value.addAll("Gérant", "Caissier", "Livreur");
        profil.setItems(value);
        profil.getSelectionModel().select(0);
        try {
            listBoutique = bout.boutiqueList();
        } catch (RemoteException ex) {
            Logger.getLogger(EnregistrerEmployeController.class.getName()).log(Level.SEVERE, null, ex);
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
                if (Notification.confirmDialog("Veuillez confirmer l'ajout du nouvel employé " + prenom.getText() + " " + nom.getText(), "Enregistrement")) {
                    try {
                        personne.setDatenaiss(dateNaissance.getValue().toString());
                        personne.setLieunaiss(lieuNaissance.getText());

                        personne.setProfil("employe");
                        personne.setSession(false);
                        personne.setBloque(0);
                        personne.setNombreconnexion(0);
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
                            personne.setCodepersonne(GenerateCode.clefUTC("GER"));
                            g.setEmploye(emp);
                            gh.persist(g);
                        } else if (profil.getSelectionModel().getSelectedItem().equals("Caissier")) {
                            personne.setCodepersonne(GenerateCode.clefUTC("CAI"));
                            c.setEmploye(emp);
                            ch.persist(c);
                        } else if (profil.getSelectionModel().getSelectedItem().equals("Livreur")) {
                            personne.setCodepersonne(GenerateCode.clefUTC("LIV"));
                            l.setEmploye(emp);
                            lh.persist(l);
                        }
                        tableEmployeList.add(new TableEmploye(
                                emp.getPersonne().getCodepersonne(),
                                emp.getPersonne().getNom(),
                                emp.getPersonne().getPrenom(),
                                emp.getFonction(),
                                emp.getPersonne().getEmail(),
                                emp.getPersonne().getTelephone(),
                                emp.getPersonne().getAdresse(),
                                emp.getPersonne().getDatenaiss(),
                                emp.getPersonne().getLieunaiss()
                        )
                        );
                        Notification.notificationSuccess("Insertion", "L'employe a été bien enregistré");
                        annuler();
                    } catch (NullPointerException e) {
                        reponse.setText("Date de naissance Obligatoire!");
                        e.printStackTrace();
                    } catch (HibernateException err) {
                        Notification.notificationErr("Erreur", "Problème avec le Serveur Base de données");
                    }
                }
            }
        } catch (Exception err) {
        }
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
    private void textValidator(KeyEvent event) {

    }

    public void removeContainsTable() {
        try {
            tableEmploye.getRoot().getChildren().clear();
        } catch (Exception e) {
        }
    }

    public void putContainsTable() {
        try {
            List<Employe> emps = eph.ListEmployes(ah.findById(admin.getCodepersonne()).getBoutiques());
            Iterator empIt = emps.iterator();
            while (empIt.hasNext()) {
                Employe emplElm = (Employe) empIt.next();
                tableEmployeList.add(new TableEmploye(
                        emplElm.getPersonne().getCodepersonne(),
                        emplElm.getPersonne().getNom(),
                        emplElm.getPersonne().getPrenom(),
                        emplElm.getFonction(),
                        emplElm.getPersonne().getEmail(),
                        emplElm.getPersonne().getTelephone(),
                        emplElm.getPersonne().getAdresse(),
                        emplElm.getPersonne().getDatenaiss(),
                        emplElm.getPersonne().getLieunaiss()
                )
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void replace(Personne personne) {
        tableEmploye.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tableEmploye.getSelectionModel().getSelectedItem());
        tableEmployeList.add(new TableEmploye(
                personne.getCodepersonne(),
                personne.getNom(),
                personne.getPrenom(),
                personne.getEmploye().getFonction(),
                personne.getEmail(),
                personne.getTelephone(),
                personne.getAdresse(),
                personne.getDatenaiss(),
                personne.getLieunaiss()
        )
        );
    }

}
