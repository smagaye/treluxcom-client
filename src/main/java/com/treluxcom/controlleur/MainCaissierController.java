package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Panier;
import com.treluxcom.metier.Paniercaissier;
import com.treluxcom.metier.Personne;
import com.treluxcom.metier.Produit;
import com.treluxcom.service.IFamilleHome;
import com.treluxcom.service.IPanierHome;
import com.treluxcom.service.IPaniercaissierHome;
import com.treluxcom.service.IProduitHome;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.SimpleFamilleCell;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainCaissierController implements Initializable {

    private Personne personne;
    @FXML
    private Label nomComplet;
    @FXML
    private Label nomination;
    @FXML
    private Label nomBoutique;
    @FXML
    private AnchorPane parent;

    @FXML
    private JFXTreeTableView<Table> tableProduit;

    @FXML
    private JFXComboBox<Famille> famille;

    @FXML
    private Label montant;

    @FXML
    private TextField quantite;

    @FXML
    private JFXButton imprimerFacture;

    @FXML
    private Pane plaqueMontant;

    @FXML
    private Label montantTotal;
    @FXML
    private JFXButton txt7;
    @FXML
    private JFXButton txt8;
    @FXML
    private JFXButton txt9;
    @FXML
    private JFXButton txt4;
    @FXML
    private JFXButton txt5;
    @FXML
    private JFXButton txt6;
    @FXML
    private JFXButton txt1;
    @FXML
    private JFXButton txt2;
    @FXML
    private JFXButton txt3;
    @FXML
    private JFXButton txt0;
    @FXML
    private JFXButton txt00;
    @FXML
    private JFXButton txt000;

    private HashMap<String, Famille> familleSelectionnee = new HashMap<String, Famille>();

    IPanierHome panhome;

    IPaniercaissierHome pancaishome;

    ApplicationContext context;

    Panier panier;

    Paniercaissier paniercaissier;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        montantTotal.setText("0");
        numerique();
        try {
            quantite.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,7}(\\d{12})?")) {
                        quantite.setText(oldValue);
                    }
                }
            });
            makeStageDrageable();
            new InitThread().start();
            famHome = (IFamilleHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiFamilleHome");
            produitHome = (IProduitHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiProduitHome");
            pancaishome = (IPaniercaissierHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPaniercaissierHome");
            panhome = (IPanierHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPanierHome");
            initTableviewLigneCommande();
            initComboBox();
            context = new ClassPathXmlApplicationContext("spring.xml");
            panier = context.getBean("panier", Panier.class);
            paniercaissier = context.getBean("paniercaissier", Paniercaissier.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    IFamilleHome famHome;
    IProduitHome produitHome;

    @FXML
    void btnAnnulerAchat(ActionEvent event) {
        try {
            famille.getSelectionModel().selectFirst();
            quantite.setText("0");
            montant.setText("0");
            montantTotal.setText("0");
            tableProduit.getRoot().getChildren().clear();
            nbreLigne = 0;
            initComboBox();
        } catch (Exception e) {
        }
    }

    @FXML
    void btnImprimer(MouseEvent event) {
        //Impression Facture
    }

    @FXML
    void btnValiderAchat(ActionEvent event) {
        try {
            //Inserer le tableProduitau dans la base de donnees
            if (nbreLigne > 0) {
                if (Notification.confirmDialog("Voulez-vous vraiment enregistrer l'achat ?", "Confimation de l'Achat")) {
                    for (int i = 0; i < nbreLigne; i++) {
                        try {
                            Table tFam = tableProduit.getSelectionModel().getModelItem(i).getValue();
                            Set prods = new HashSet(produitHome.retirerProduit(familleSelectionnee.get(tFam.numero.get()), Integer.parseInt(tFam.quantite.get())));

                            panier.setCodepanier(GenerateCode.clefUTC("PAN"));
                            panier.setDatepanier(new Date());
                            panier.setPrix(new BigDecimal(tFam.prixTotal.get()));
                            panier.setProduits(prods);

                            paniercaissier.setPanier(panier);
                            paniercaissier.setCaissier(personne.getEmploye().getCaissier());
                            paniercaissier.setFacture(panier.getCodepanier() +"."+ "pdf");

                            pancaishome.persist(paniercaissier);
                            Notification.notificationSuccess("Alert", "Done");
                        } catch (Exception excep) {
                        }
                    }
                }
            } else {
                Notification.notificationErr("Alert", "Veuillez Choisir des produits");
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

    ObservableList<Table> tablelist = FXCollections.observableArrayList();
    int nbreLigne = 0;

    @FXML
    private void btnPlus(ActionEvent event) {
        Double pt = null;
        Famille fam;
        try {
            fam = famille.getSelectionModel().selectedItemProperty().get();
            if (fam == null) {
                Notification.notificationErr("Alerte", "Veuillez choisir un produit");
            } else if (quantite.getText() == null || quantite.getText().equals("0")) {
                Notification.notificationErr("Alerte", "Veuillez specifier la quantite");
            } else if (montant.getText().equals("0")) {
                Notification.notificationErr("Alerte", "Veuillez choisir un produit");
            } else if (familleSelectionnee.containsKey(fam.getCodefamille())) {
                Notification.notificationErr("Alerte", "ignorer");
            } else {
                Double pu = Double.parseDouble(montant.getText());
                Double qt = Double.parseDouble(quantite.getText());
                pt = qt * pu;
                Double mtTotal = Double.parseDouble(montantTotal.getText()) + pt;
                montantTotal.setText(mtTotal.toString());

                tableProduitList.addAll(new Table(
                        famille.getSelectionModel().getSelectedItem().getCodefamille(),
                        famille.getSelectionModel().getSelectedItem().getNom(),
                        quantite.getText(),
                        montant.getText(),
                        Double.toString(pt)
                ));
                nbreLigne += 1;
                // famille.getItems().remove(fam);
            }
            familleSelectionnee.put(fam.getCodefamille(), fam);
        } catch (Exception numberFormatException) {
           
        }
    }

    @FXML
    private void btnExit(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void btnNumerique(ActionEvent event) {
    }

    @FXML
    private void btnReset(ActionEvent event) {
        String qtTxt = quantite.getText();
        if (!qtTxt.isEmpty()) {
            quantite.setText("");
        }
    }

    @FXML
    private void btnClear(ActionEvent event) {
        String qtTxt = quantite.getText();
        if (!qtTxt.isEmpty()) {
            quantite.setText(qtTxt.substring(0, qtTxt.length() - 1));
        }
    }

    @FXML
    private void mnSupprimer(ActionEvent event) {
        try {
            TreeItem<Table> tFam = tableProduit.getSelectionModel().getSelectedItem();

            Famille fa = familleSelectionnee.get(tFam.getValue().numero.get());

            familleSelectionnee.remove(fa.getCodefamille());
            // famille.getItems().add(fa);

            Double prixTotal = Double.parseDouble(tFam.getValue().prixTotal.get());
            Double ptt = Double.parseDouble(montantTotal.getText()) - prixTotal;
            montantTotal.setText(ptt.toString());
            tableProduit.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tFam);
            nbreLigne -= 1;

        } catch (Exception exception) {
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
                    nomComplet.setText(personne.getPrenom() + " " + personne.getNom());
                    nomination.setText("Caissi" + ((personne.getSexe().equals("m") || personne.getSexe().equals("M")) ? "er" : "ère") + " à ");
                    nomBoutique.setText(personne.getEmploye().getBoutique().getNom());
                });
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

    }

    private void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ConnexionController.stageBack.setX(event.getScreenX() - xOffset);
                ConnexionController.stageBack.setY(event.getScreenY() - yOffset);
                ConnexionController.stageBack.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            ConnexionController.stageBack.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            ConnexionController.stageBack.setOpacity(1.0f);
        });

    }
    private List<Famille> listFamille = new ArrayList();
    private ObservableList obslistFamille;

    private void numerique() {
        txt0.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt0.getText());
        });
        txt00.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt00.getText());
        });
        txt000.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt000.getText());
        });
        txt1.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt1.getText());
        });
        txt2.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt2.getText());
        });
        txt3.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt3.getText());
        });
        txt4.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt4.getText());
        });
        txt5.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt5.getText());
        });
        txt6.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt6.getText());
        });
        txt7.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt7.getText());
        });
        txt8.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt8.getText());
        });
        txt9.setOnAction((e) -> {
            quantite.setText(quantite.getText() + txt9.getText());
        });

    }

    private void initComboBox() {
        try {
            List<Famille> lf = famHome.familleList();
            for (Famille f : lf) {
                try {
                    Produit prod = produitHome.produitDispoList(f).get(0);
                    listFamille.add(f);
                } catch (RemoteException remoteException) {
                } catch (IndexOutOfBoundsException re) {
                }
            }

            famille.setButtonCell(new SimpleFamilleCell());

            obslistFamille = FXCollections.observableList(listFamille);
            famille.setItems(obslistFamille);
            famille.setCellFactory(listView -> new SimpleFamilleCell());

            famille.setOnAction((e) -> {
                quantite.setText("");
                Famille fam = famille.getSelectionModel().getSelectedItem();
                try {
                    Produit prod = produitHome.produitDispoList(fam).get(0);
                    montant.setText(prod.getPrixgerant().toString());
                } catch (RemoteException ex) {
                    Notification.notificationSuccess("Alerte", "Probleme Reseau!");
                } catch (IndexOutOfBoundsException er) {
                    famille.getItems().remove(fam);
                } catch (NullPointerException np) {
                    famille.getItems().remove(fam);
                }
            });
        } catch (Exception ex) {

        }

    }

    class Table extends RecursiveTreeObject<Table> {

        StringProperty numero;
        StringProperty quantite;
        StringProperty designation;
        StringProperty prixUnitaire;
        StringProperty prixTotal;

        public Table(String numero, String designation, String quantite, String prixUnitaire, String prixTotal) {
            this.numero = new SimpleStringProperty(numero);
            this.designation = new SimpleStringProperty(designation);
            this.quantite = new SimpleStringProperty(quantite);
            this.prixTotal = new SimpleStringProperty(prixTotal);
            this.prixUnitaire = new SimpleStringProperty(prixUnitaire);
        }
    }
    ObservableList<Table> tableProduitList = FXCollections.observableArrayList();

    public void initTableviewLigneCommande() {

        try {
            JFXTreeTableColumn<Table, String> numerocol = new JFXTreeTableColumn<>("Numero");
            numerocol.setPrefWidth(90);
            numerocol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                    return param.getValue().getValue().numero;
                }
            });

            JFXTreeTableColumn<Table, String> designationcol = new JFXTreeTableColumn<>("Designation");
            designationcol.setPrefWidth(150);
            designationcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                    return param.getValue().getValue().designation;
                }
            });

            JFXTreeTableColumn<Table, String> quantitecol = new JFXTreeTableColumn<>("Quantité");
            quantitecol.setPrefWidth(90);
            quantitecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                    return param.getValue().getValue().quantite;
                }
            });

            JFXTreeTableColumn<Table, String> prixUnitairecol = new JFXTreeTableColumn<>("Prix Unitaire");
            prixUnitairecol.setPrefWidth(100);
            prixUnitairecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                    return param.getValue().getValue().prixUnitaire;
                }
            });

            JFXTreeTableColumn<Table, String> prixTotalcol = new JFXTreeTableColumn<>("Prix total");
            prixTotalcol.setPrefWidth(100);
            prixTotalcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                    return param.getValue().getValue().prixTotal;
                }
            });

            TreeItem<Table> root1 = new RecursiveTreeItem<Table>(tableProduitList, RecursiveTreeObject::getChildren);
            tableProduit.getColumns().setAll(numerocol, designationcol, quantitecol, prixUnitairecol, prixTotalcol);
            tableProduit.setRoot(root1);
            tableProduit.setShowRoot(false);
            tableProduit.getSelectionModel().select(0);

        } catch (Exception err) {
            Notification.notificationErr("Erreur", "Problème lors du chargement des données");
        }

    }

}
