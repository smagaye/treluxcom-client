package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.treluxcom.metier.Boutique;
import com.treluxcom.service.IAdministrateurHome;
import com.treluxcom.service.IBoutiqueHome;
import com.treluxcom.utilitaire.DateManager;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.hibernate.HibernateException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MesBoutiquesController implements Initializable {

    Boutique boutique;
    IBoutiqueHome boutiqueHome;
    IAdministrateurHome adminHome;

    @FXML
    private TextField email;

    @FXML
    private TextField nomBoutique;

    @FXML
    private TextField adresse;

    @FXML
    private TextField pays;

    @FXML
    private TextField telephone;

    @FXML
    private TextField ville;

    @FXML
    private JFXTreeTableView<TableBoutique> tableBoutique;

    @FXML
    private JFXTextField recherche;
    @FXML
    private Pane ajoutFamille;
    @FXML
    private Label lbNomBoutique;
    @FXML
    private Label lbEmail;
    @FXML
    private Label lbTelephone;
    @FXML
    private Label lbAdresse;
    @FXML
    private Label lbVille;
    @FXML
    private Label lbPays;
    @FXML
    private Pane plaque;

    @FXML
    private void mnEditer(ActionEvent event) {
    }

    @FXML
    private void mnSupprimer(ActionEvent event) {
        TableBoutique te = tableBoutique.getSelectionModel().getSelectedItem().getValue();
        try {
            boutiqueHome.deleteById(te.codeBoutique.get());
            tableBoutique.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tableBoutique.getSelectionModel().getSelectedItem());
            Notification.notificationSuccess("Suppression", "La Boutique a été supprimée avec succès");
        } catch (HibernateException err) {
            Notification.notificationErr("Suppression", "Echoué");
            err.printStackTrace();
        } catch (RemoteException ex) {
            Logger.getLogger(MesBoutiquesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void btnEnregistrer(ActionEvent event) {        
            try {
                if (nomBoutique.getText().isEmpty()) {
                    Notification.notificationErr("Erreur", "le Champ 'Nom Boutique' est obligatoire");
                } else if (adresse.getText().isEmpty()) {
                    Notification.notificationErr("Erreur", "le Champ 'Adresse' est obligatoire");
                } else if (email.getText().isEmpty()) {
                    Notification.notificationErr("Erreur", "le Champ 'Email' est obligatoire");
                } else if (ville.getText().isEmpty()) {
                    Notification.notificationErr("Erreur", "le Champ 'Ville' est obligatoire");
                } else if (telephone.getText().isEmpty()) {
                    Notification.notificationErr("Erreur", "le Champ 'Téléphone' est obligatoire");
                } else if (pays.getText().isEmpty()) {
                    Notification.notificationErr("Erreur", "le Champ 'Pays' est obligatoire");
                } else {
                    if (Notification.confirmDialog("Veuillez confirmer l'ajout de la boutique "+ nomBoutique.getText(), "Enregistrement"))  {
                        boutique.setCodeboutique(GenerateCode.clefUTC("BOU"));
                        boutique.setDatecreation(DateManager.getDate());
                        boutique.setNom(nomBoutique.getText());
                        boutique.setAdresse(adresse.getText());
                        boutique.setEmail(email.getText());
                        boutique.setVille(ville.getText());
                        boutique.setTelephone(telephone.getText());
                        boutique.setPays(pays.getText());
                        boutique.setAdministrateur(adminHome.administrateurList().get(0));
                        boutiqueHome.persist(boutique);
                        
                        tableBoutiqueList.add(new TableBoutique(
                                boutique.getNom(),
                                boutique.getAdresse(),
                                boutique.getTelephone(),
                                boutique.getEmail(),
                                boutique.getVille(),
                                boutique.getPays(),
                                boutique.getCodeboutique()
                        )
                        );
                    }
                }
            } catch (Exception err) {
                err.printStackTrace();
            }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        boutique = context.getBean("boutique", Boutique.class);
        try {
            boutiqueHome = (IBoutiqueHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiBoutiqueHome");
            adminHome = (IAdministrateurHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiAdministrateurHome");
            initTableviewBoutique();

            recherche.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    tableBoutique.setPredicate(new Predicate<TreeItem<TableBoutique>>() {
                        @Override
                        public boolean test(TreeItem<TableBoutique> t) {
                            Boolean flag = t.getValue().boutique.getValue().contains(newValue) || t.getValue().adresse.getValue().contains(newValue) || t.getValue().ville.getValue().contains(newValue) || t.getValue().telephone.getValue().contains(newValue);
                            return flag;
                        }
                    }
                    );
                }
            });

            tableBoutique.setOnMouseClicked((e) -> {
                
                TableBoutique tb = tableBoutique.getSelectionModel().getSelectedItem().getValue();
                lbNomBoutique.setText(tb.boutique.get());
                lbAdresse.setText(tb.adresse.get());
                lbVille.setText(tb.ville.get());
                lbPays.setText(tb.pays.get());
                lbTelephone.setText(tb.telephone.get());
                plaque.setVisible(true);
            });
            tableBoutique.setOnKeyReleased((e) -> {
                plaque.setVisible(true);
                TableBoutique tb = tableBoutique.getSelectionModel().getSelectedItem().getValue();
                lbNomBoutique.setText(tb.boutique.get());
                lbAdresse.setText(tb.adresse.get());
                lbVille.setText(tb.ville.get());
                lbPays.setText(tb.pays.get());
                lbTelephone.setText(tb.telephone.get());
                plaque.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TableBoutique extends RecursiveTreeObject<TableBoutique> {

        StringProperty boutique;
        StringProperty email;
        StringProperty adresse;
        StringProperty ville;
        StringProperty telephone;
        StringProperty pays;
        StringProperty mineral;
        StringProperty codeBoutique;

        public TableBoutique(String boutique, String adresse, String telephone, String email, String ville, String pays, String codeBoutique) {
            this.boutique = new SimpleStringProperty(boutique);
            this.adresse = new SimpleStringProperty(adresse);
            this.email = new SimpleStringProperty(email);
            this.ville = new SimpleStringProperty(ville);
            this.pays = new SimpleStringProperty(pays);
            this.codeBoutique = new SimpleStringProperty(codeBoutique);
            this.telephone = new SimpleStringProperty(telephone);
            this.codeBoutique = new SimpleStringProperty(codeBoutique);
        }
    }
    ObservableList<TableBoutique> tableBoutiqueList = FXCollections.observableArrayList();

    public void initTableviewBoutique() {

        try {
            JFXTreeTableColumn<TableBoutique, String> boutiquecol = new JFXTreeTableColumn<>("Boutique");
            boutiquecol.setPrefWidth(180);
            boutiquecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBoutique, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBoutique, String> param) {
                    return param.getValue().getValue().boutique;
                }
            });
            JFXTreeTableColumn<TableBoutique, String> adressecol = new JFXTreeTableColumn<>("Adresse");
            adressecol.setPrefWidth(130);
            adressecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBoutique, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBoutique, String> param) {
                    return param.getValue().getValue().adresse;
                }
            });

            JFXTreeTableColumn<TableBoutique, String> emailcol = new JFXTreeTableColumn<>("E-mail");
            emailcol.setPrefWidth(150);
            emailcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBoutique, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBoutique, String> param) {
                    return param.getValue().getValue().email;
                }
            });

            JFXTreeTableColumn<TableBoutique, String> villecol = new JFXTreeTableColumn<>("Ville");
            villecol.setPrefWidth(100);
            villecol.setSortable(true);
            villecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBoutique, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBoutique, String> param) {
                    return param.getValue().getValue().ville;
                }
            });
            JFXTreeTableColumn<TableBoutique, String> telephonecol = new JFXTreeTableColumn<>("Téléphone");
            telephonecol.setPrefWidth(100);
            telephonecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBoutique, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBoutique, String> param) {
                    return param.getValue().getValue().telephone;
                }
            });
            JFXTreeTableColumn<TableBoutique, String> codeBoutiquecol = new JFXTreeTableColumn<>("Code Boutique");
            codeBoutiquecol.setPrefWidth(100);
            codeBoutiquecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBoutique, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBoutique, String> param) {
                    return param.getValue().getValue().codeBoutique;
                }
            });
            JFXTreeTableColumn<TableBoutique, String> payscol = new JFXTreeTableColumn<>("Pays");
            payscol.setPrefWidth(100);
            payscol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableBoutique, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableBoutique, String> param) {
                    return param.getValue().getValue().pays;
                }
            });

            List<Boutique> boutiqueRefresh = new ArrayList<Boutique>();
            boutiqueRefresh = boutiqueHome.boutiqueList();
            Iterator boutiques = boutiqueRefresh.iterator();
            while (boutiques.hasNext()) {
                Boutique bout = (Boutique) boutiques.next();
                tableBoutiqueList.add(new TableBoutique(
                        bout.getNom(),
                        bout.getAdresse(),
                        bout.getTelephone(),
                        bout.getEmail(),
                        bout.getVille(),
                        bout.getPays(),
                        bout.getCodeboutique()
                )
                );
            }
            TreeItem<TableBoutique> root1 = new RecursiveTreeItem<TableBoutique>(tableBoutiqueList, RecursiveTreeObject::getChildren);
            tableBoutique.getColumns().setAll(boutiquecol, adressecol, telephonecol, emailcol, villecol, payscol);
            tableBoutique.setRoot(root1);
            tableBoutique.setShowRoot(false);
            tableBoutique.getSelectionModel().select(0);

        } catch (Exception err) {
            Notification.notificationErr("Erreur", "Problème lors du chargement des données");
            err.printStackTrace();
        }

    }

}
