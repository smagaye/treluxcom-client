package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.treluxcom.metier.Commande;
import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Fournisseur;
import com.treluxcom.metier.Gerant;
import com.treluxcom.metier.Lignecommande;
import com.treluxcom.metier.LignecommandeId;
import com.treluxcom.service.ICommandeHome;
import com.treluxcom.service.IFamilleHome;
import com.treluxcom.service.ILignecommandeHome;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.SimpleFamilleCell;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PasserCommandeController implements Initializable {

    @FXML
    private ComboBox<Famille> famille;
    List<Famille> listFamille = new ArrayList<>();
    ObservableList<Famille> obslistFamille;
    @FXML
    private Label lbSucree;
    @FXML
    private Label lbNaturelle;
    @FXML
    private Label lbFormat;
    @FXML
    private Label lbAlcoolisee;
    @FXML
    private Label lbGazeuse;
    @FXML
    private Label lbSaveur;
    @FXML
    private Label lbDegreAlcool;
    @FXML
    private Label lbNomFamille;
    @FXML
    private Label lbDescription;
    @FXML
    private Label lbMinerale;
    @FXML
    private Pane paneVisualisation;
    @FXML
    JFXTreeTableView<TableLigneCommande> tableLigneCommande;
    private Fournisseur fournisseur;
    private Gerant gerant;
    @FXML
    private TextField conditionnement;
    @FXML
    private TextField prixUnitaire;
    @FXML
    private TextField quantite;
    @FXML
    private ToggleButton tva;

    public void setGerant(Gerant gerant) {
        this.gerant = gerant;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }
    @FXML
    private Label pTotal;
    int nbreLigne = 0;

    @FXML
    public void btnAjouter(ActionEvent e) {
        Double prixTotal = 0.0;
        Double pt = 0.0;
        if (quantite.getText().isEmpty()) {
            Notification.notificationErr("Erreur", "Le champ 'Quantité' est obligatoire");
        } else if (conditionnement.getText().isEmpty()) {
            Notification.notificationErr("Erreur", "Le champ 'Conditionnement' est obligatoire");
        } else if (prixUnitaire.getText().isEmpty()) {
            Notification.notificationErr("Erreur", "Le champ 'Prix Unitaire' est obligatoire");
        } else {
            Double pu = Double.parseDouble(prixUnitaire.getText());
            Double qt = Double.parseDouble(quantite.getText());
            Double cond = Double.parseDouble(conditionnement.getText());

            String tvaValue;
            if (tva.isSelected()) {
                tvaValue = "oui";
                pt = (cond * qt * pu) * 0.18 + (cond * qt * pu);
            } else {
                tvaValue = "non";
                pt = cond * qt * pu;
            }

            tableLigneCommandeList.addAll(new TableLigneCommande(
                    famille.getSelectionModel().getSelectedItem().getCodefamille(),
                    famille.getSelectionModel().getSelectedItem().getNom(),
                    conditionnement.getText(),
                    quantite.getText(),
                    tvaValue,
                    prixUnitaire.getText(),
                    pt.toString()
            ));
            prixTotal += pt;
            Double ptt = Double.parseDouble(pTotal.getText()) + pt;
            pTotal.setText(ptt.toString());
            nbreLigne += 1;
        }
    }

    IFamilleHome famHome;
    ICommandeHome comHome;
    ILignecommandeHome lignecomHome;
    Commande com;
    LignecommandeId lignecomId;
    Lignecommande lignecom;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pTotal.setText("0");
        //Chargement des Services
        try {
            quantite.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,7}([\\.]\\d{0})?")) {
                        quantite.setText(oldValue);
                    }
                }
            });

            conditionnement.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,7}([\\.]\\d{0})?")) {
                        conditionnement.setText(oldValue);
                    }
                }
            });
            
                        prixUnitaire.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,7}([\\.]\\d{0})?")) {
                        prixUnitaire.setText(oldValue);
                    }
                }
            });
                        
            famHome = (IFamilleHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiFamilleHome");
            comHome = (ICommandeHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiCommandeHome");
            lignecomHome = (ILignecommandeHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiLignecommandeHome");
        } catch (Exception ex) {
        }
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        com = context.getBean("commande", Commande.class);
        lignecomId = context.getBean("lignecommandeId", LignecommandeId.class);
        lignecom = context.getBean("lignecommande", Lignecommande.class);

        try {
            initTableviewLigneCommande();
            initComboBox();
            famille.setOnAction((e) -> {
                paneVisualisation.setVisible(true);
                Famille fam = famille.getSelectionModel().getSelectedItem();
                lbSaveur.setText("- " + fam.getSaveur());
                lbFormat.setText("- Format: " + fam.getFormat());
                lbNomFamille.setText(fam.getNom());
                lbDescription.setText(fam.getLibelle());
                lbDegreAlcool.setText("- Degré d'alcool: " + fam.getDegresalcool().toString());
                if (fam.getSucre().equals(true)) {
                    lbSucree.setText("- Sucrée");
                } else {
                    lbSucree.setText("- Non Sucrée");
                }
                if (fam.getNaturel().equals(true)) {
                    lbNaturelle.setText("- Naturelle");
                } else {
                    lbNaturelle.setText("- Non Naturelle");
                }
                if (fam.getMineral().equals(true)) {
                    lbMinerale.setText("- Minérale");
                } else {
                    lbMinerale.setText("- Non Minérale");
                }
                if (fam.getAlcoolise().equals(true)) {
                    lbAlcoolisee.setText("- Alcoolisée");
                } else {
                    lbAlcoolisee.setText("- Non Alcoolisée");
                }
                if (fam.getGazeuse().equals(true)) {
                    lbGazeuse.setText("- Gazeuse");
                } else {
                    lbGazeuse.setText("- Non Gazeuse");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComboBox() {
        try {
            listFamille = famHome.familleList();
        } catch (RemoteException ex) {
            Logger.getLogger(PasserCommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        famille.setButtonCell(new SimpleFamilleCell());

        obslistFamille = FXCollections.observableList(listFamille);
        famille.setItems(obslistFamille);
        famille.setCellFactory(listView -> new SimpleFamilleCell());
        famille.getSelectionModel().select(0);

    }

    private void btnClose(ActionEvent e) {
        ((Node) e.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void btnAnnuler(ActionEvent e) {
        tableLigneCommande.getRoot().getChildren().clear();
        quantite.setText("");
        conditionnement.setText("");
        prixUnitaire.setText("");
        nbreLigne = 0;
        pTotal.setText("0.0");
    }

    @FXML
    private void btnVider(ActionEvent e) {
        quantite.setText("");
        conditionnement.setText("");
        prixUnitaire.setText("");
    }

    @FXML
    private void btnEnregistrer(ActionEvent event) {
        String codeCommande = GenerateCode.clefUTC("COM");
        try {
            if (nbreLigne != 0) {
                com.setCodecommande(codeCommande);
                com.setEtat(0);
                com.setDatecommande(new Date());
                com.setGerant(gerant);
                com.setFournisseur(fournisseur);
                comHome.persist(com);
                for (int i = 0; i < nbreLigne; i++) {
                    TableLigneCommande tlc = tableLigneCommande.getSelectionModel().getModelItem(i).getValue();
                    String codeFamille = tlc.codeFamille.get();

                    lignecomId.setCodecommande(codeCommande);
                    lignecomId.setCodefamille(codeFamille);

                    lignecom.setId(lignecomId);
                    lignecom.setConditionnement(new Integer(tlc.conditionnement.get()));
                    lignecom.setQuantite(new Integer(tlc.quantite.get()));
                    lignecom.setPrixunitaire(new BigDecimal(tlc.prixUnitaire.get()));
                    lignecom.setPrixtotal(new BigDecimal(tlc.prixTotal.get()));
                    lignecom.setTva((tlc.tva.get().equals("oui") ? true : false));
                    lignecom.setCommande(com);
                    lignecom.setFamille(famHome.findById(tlc.codeFamille.get()));
                    lignecomHome.persistLigneCommande(lignecom);
                }

                pTotal.setText("0.0");
                Notification.notificationSuccess("Succes", "Commande enregistée avec succes");
            } else {
                Notification.notificationErr("Erreur", "Aucune désignation n'a été spécifiée!");
            }

            quantite.setText("");
            conditionnement.setText("");
            prixUnitaire.setText("");
            nbreLigne = 0;
            tableLigneCommande.getRoot().getChildren().clear();
        } catch (RuntimeException er) {
            try {
                lignecomHome.deleteById(codeCommande);
                comHome.deleteById(com.getCodecommande());
            } catch (Exception ex) {
            }
            Notification.notificationErr("Erreur", "Vous avez désigné une même famille de produit plusieurs fois dans la commande");

        } catch (RemoteException ex) {
            Logger.getLogger(PasserCommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void mnSupprimer(ActionEvent event) {
        try {
            tableLigneCommande.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tableLigneCommande.getSelectionModel().getSelectedItem());
            Double prixTotal = Double.parseDouble(pTotal.getText()) - Double.parseDouble(tableLigneCommande.getSelectionModel().getSelectedItem().getValue().prixTotal.get());
            Double ptt = Double.parseDouble(pTotal.getText()) - prixTotal;
            pTotal.setText(ptt.toString());
            nbreLigne -= 1;
        } catch (Exception exception) {
        }
    }

    @FXML
    private void btnClose(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    class TableLigneCommande extends RecursiveTreeObject<TableLigneCommande> {

        StringProperty codeFamille;
        StringProperty famille;
        StringProperty quantite;
        StringProperty conditionnement;
        StringProperty tva;
        StringProperty prixUnitaire;
        StringProperty prixTotal;

        public TableLigneCommande(String codeFamille, String famille, String conditionnement, String quantite, String tva, String prixUnitaire, String prixTotal) {
            this.codeFamille = new SimpleStringProperty(codeFamille);
            this.famille = new SimpleStringProperty(famille);
            this.conditionnement = new SimpleStringProperty(conditionnement);
            this.quantite = new SimpleStringProperty(quantite);
            this.tva = new SimpleStringProperty(tva);
            this.prixTotal = new SimpleStringProperty(prixTotal);
            this.prixUnitaire = new SimpleStringProperty(prixUnitaire);
        }
    }
    ObservableList<TableLigneCommande> tableLigneCommandeList = FXCollections.observableArrayList();

    public void initTableviewLigneCommande() {

        try {
            JFXTreeTableColumn<TableLigneCommande, String> famillecol = new JFXTreeTableColumn<>("Famille");
            famillecol.setPrefWidth(180);
            famillecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().famille;
                }
            });
            JFXTreeTableColumn<TableLigneCommande, String> codeFamillecol = new JFXTreeTableColumn<>("code Famille");
            codeFamillecol.setPrefWidth(180);
            codeFamillecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().codeFamille;
                }
            });
            JFXTreeTableColumn<TableLigneCommande, String> conditionnementcol = new JFXTreeTableColumn<>("Conditionnement");
            conditionnementcol.setPrefWidth(130);
            conditionnementcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().conditionnement;
                }
            });

            JFXTreeTableColumn<TableLigneCommande, String> quantitecol = new JFXTreeTableColumn<>("Quantité");
            quantitecol.setPrefWidth(150);
            quantitecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().quantite;
                }
            });

            JFXTreeTableColumn<TableLigneCommande, String> tvacol = new JFXTreeTableColumn<>("TVA");
            tvacol.setPrefWidth(100);
            tvacol.setSortable(true);
            tvacol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().tva;
                }
            });
            JFXTreeTableColumn<TableLigneCommande, String> prixUnitairecol = new JFXTreeTableColumn<>("Prix Unitaire");
            prixUnitairecol.setPrefWidth(100);
            prixUnitairecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().prixUnitaire;
                }
            });

            JFXTreeTableColumn<TableLigneCommande, String> prixTotalcol = new JFXTreeTableColumn<>("Prix total");
            prixTotalcol.setPrefWidth(100);
            prixTotalcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().prixTotal;
                }
            });

            TreeItem<TableLigneCommande> root1 = new RecursiveTreeItem<TableLigneCommande>(tableLigneCommandeList, RecursiveTreeObject::getChildren);
            tableLigneCommande.getColumns().setAll(famillecol, conditionnementcol, quantitecol, tvacol, prixUnitairecol, prixTotalcol);
            tableLigneCommande.setRoot(root1);
            tableLigneCommande.setShowRoot(false);
            tableLigneCommande.getSelectionModel().select(0);

        } catch (Exception err) {
            Notification.notificationErr("Erreur", "Problème lors du chargement des données");
        }

    }

}
