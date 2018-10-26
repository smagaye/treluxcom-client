package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTreeTableColumn;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.treluxcom.metier.Commande;
import com.treluxcom.metier.Devis;
import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Fournisseur;
import com.treluxcom.metier.Lignedevis;
import com.treluxcom.metier.Produit;
import com.treluxcom.metier.Ressourcemedia;
import com.treluxcom.metier.Stock;
import com.treluxcom.service.ICommandeHome;
import com.treluxcom.service.IFamilleHome;
import com.treluxcom.service.ILignecommandeHome;
import com.treluxcom.service.IProduitHome;
import com.treluxcom.service.IStockHome;
import com.treluxcom.service.Statistiques;
import com.treluxcom.utilitaire.DateManager;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.QRCode;
import com.treluxcom.utilitaire.QRCodePDF;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.SimpleCommandeCell;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EnregistrerCommandeController implements Initializable {

    @FXML
    private TextField categorie;

    @FXML
    private DatePicker dateReception;

    @FXML
    private DatePicker dateExpiration;

    @FXML
    private TextField tarifGerant;

    @FXML
    private TextField fournisseur;

    @FXML
    private JFXTreeTableView<TableLigneCommande> tableLigneCommande;

    @FXML
    private ComboBox<Commande> commande;

    @FXML
    private Pane aucunElementSelect;

    @FXML
    private Pane paneVisualisation;

    @FXML
    private Label lbNomFamille;

    @FXML
    private Label lbSucree;

    @FXML
    private Label lbAlcoolisee;

    @FXML
    private Label lbNaturelle;

    @FXML
    private Label lbFormat;

    @FXML
    private Label lbSaveur;

    @FXML
    private Label lbMinerale;

    @FXML
    private Label lbGazeuse;

    @FXML
    private TitledPane paneStock;

    @FXML
    private Label referenceStock;

    @FXML
    private Label quantiteTotal;

    @FXML
    private Label nomBoutique;

    @FXML
    private Label dateCreationStock;

    @FXML
    private Label auteur;

    IFamilleHome famHome;
    ICommandeHome comHome;
    ILignecommandeHome lignecomHome;
    ApplicationContext context;
    Statistiques st;
    IStockHome stoHome;
    IProduitHome prodHome;
    Stock sto;
    @FXML
    private TextField benefice;
    @FXML
    private Label balance;
    @FXML
    private ComboBox<?> famille;
    @FXML
    private ImageView photoFamille;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tarifGerant.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,7}([\\.]\\d{0})?")) {
                        tarifGerant.setText(oldValue);
                    }
                }
            });
            famHome = (IFamilleHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiFamilleHome");
            comHome = (ICommandeHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiCommandeHome");
            st = (Statistiques) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiStatistiques");
            stoHome = (IStockHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiStockHome");
            prodHome = (IProduitHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiProduitHome");

            context = new ClassPathXmlApplicationContext("spring.xml");
            sto = context.getBean("stock", Stock.class);
            initComboBox();
            initTableviewLigneCommande();
        } catch (Exception ex) {
            ex.getMessage();
        }

        try {
            tableLigneCommande.setOnMouseClicked((e) -> {
                paneVisualisation.setVisible(true);
                aucunElementSelect.setVisible(false);
                TableLigneCommande tl = null;
                try {
                    tl = tableLigneCommande.getSelectionModel().getSelectedItem().getValue();
                } catch (Exception er) {
                }
                Famille tf = context.getBean("famille", Famille.class);
                try {
                    tf = famHome.findById(tl.codeFamille.get());
                } catch (Exception ex) {
                }
                lbSaveur.setText("- " + tf.getSaveur());
                lbFormat.setText("- Format: " + tf.getFormat());
                lbNomFamille.setText(tf.getNom());

                if (tf.getSucre().equals("true")) {
                    lbSucree.setText("- Sucrée");
                } else {
                    lbSucree.setText("- Non Sucrée");
                }
                if (tf.getNaturel().equals("true")) {
                    lbNaturelle.setText("- Naturelle");
                } else {
                    lbNaturelle.setText("- Non Naturelle");
                }
                if (tf.getMineral().equals("true")) {
                    lbMinerale.setText("- Minérale");
                } else {
                    lbMinerale.setText("- Non Minérale");
                }
                if (tf.getAlcoolise().equals("true")) {
                    lbAlcoolisee.setText("- Alcoolisée");
                } else {
                    lbAlcoolisee.setText("- Non Alcoolisée");
                }
                if (tf.getGazeuse().equals("true")) {
                    lbGazeuse.setText("- Gazeuse");
                } else {
                    lbGazeuse.setText("- Non Gazeuse");
                }

                try {
                    Ressourcemedia res = (Ressourcemedia) new ArrayList(tf.getRessourcemedias()).get(0);
                    BufferedImage bufferedImage;
                    bufferedImage = ImageIO.read(new File("src/main/resources/images/ressourceMedia/" + res.getLocalisation()));
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    this.photoFamille.setImage(image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                fournisseur.setText(tl.fournisseur.get());
                tarifGerant.setText(tl.tarifGerant.get());
                categorie.setText(tl.categorie.get());
                dateReception.setValue(LocalDate.now());
                dateExpiration.setValue(LocalDate.now().plusYears(3));

                if (tarifGerant.getText().isEmpty()) {
                    balance.setText("Perte : ");
                    benefice.setText(tl.prixTotal.get());
                } else {
                    Double bal = 0.0, tarifFour = 0.0;
                    bal = Double.parseDouble(tarifGerant.getText());
                    tarifFour = Double.parseDouble(tl.prixTotal.get());
                    bal = bal * Double.parseDouble(tl.quantite.get()) * Double.parseDouble(tl.conditionnement.get());
                    if (tarifFour > bal) {
                        balance.setText("Perte : ");
                        benefice.setText(((Double) (tarifFour - bal)).toString());
                    } else {
                        balance.setText("Benefice ");
                        benefice.setText(((Double) (bal - tarifFour)).toString());
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void btnModifier(ActionEvent event) {
        if (tarifGerant.getText().isEmpty()) {
            Notification.notificationErr("Erreur", "Le Champ Tarif gerant est Obligatoire");
        } else if (categorie.getText().isEmpty()) {
            Notification.notificationErr("Erreur", "Le Champ categorie est Obligatoire");
        } else if (dateReception.getValue().toString().isEmpty()) {
            Notification.notificationErr("Erreur", "Veuillez preciser la Date de reception est Obligatoire");
        } else if (dateExpiration.getValue().toString().isEmpty()) {
            Notification.notificationErr("Erreur", "Veuillez preciser la Date d'expiration est Obligatoire");
        } else {
            TableLigneCommande tl = tableLigneCommande.getSelectionModel().getSelectedItem().getValue();
            tl.categorie.setValue(categorie.getText());
            tl.tarifGerant.setValue(tarifGerant.getText());
            tl.dateReception.setValue(dateReception.getValue().toString());
            tl.dateExpiration.setValue(dateExpiration.getValue().toString());
            Notification.notificationSuccess("Modification", "Vos modifications ont ete bien prise en compte!");

        }
    }

    @FXML
    private void comboCommandeEvent(ActionEvent event) throws RemoteException {
        listCommande = comHome.commandeValideList();
        Commande co;
        List<Devis> listdevis;
        Devis devis;
        try {
            co = commande.getSelectionModel().getSelectedItem();
            listdevis = new ArrayList(co.getDevises());
            devis = context.getBean("devis", Devis.class);
            try {
                devis = listdevis.get(0);
            } catch (Exception e) {
            
            }
            Double qt = 0.0;
            referenceStock.setText(GenerateCode.clefUTC("STO"));
            nomBoutique.setText(co.getGerant().getEmploye().getBoutique().getNom());
            dateCreationStock.setText(DateManager.getDate());
            auteur.setText(co.getGerant().getEmploye().getPersonne().getPrenom() + " " + co.getGerant().getEmploye().getPersonne().getNom());
            aucunElementSelect.setVisible(true);
            paneVisualisation.setVisible(false);
            Iterator lignedevis = devis.getLignedevises().iterator();
            tableLigneCommandeList.clear();
            while (lignedevis.hasNext()) {
                Lignedevis ld = (Lignedevis) lignedevis.next();
                if (ld.getQuantite() != 0) {
                    qt += ld.getQuantite().doubleValue() * ld.getConditionnement().doubleValue();
                    tableLigneCommandeList.addAll(new TableLigneCommande(
                            ld.getFamille().getCodefamille(),
                            ld.getFamille().getNom(),
                            ld.getConditionnement().toString(),
                            ld.getQuantite().toString(),
                            (ld.getTva()) ? "Oui" : "Non",
                            ld.getPrixunitaire().toString(),
                            ld.getPrixtotal().toString(),
                            ld.getDevis().getFournisseur().getPersonne().getPrenom() + " " + ld.getDevis().getFournisseur().getPersonne().getNom()
                    ));
                }
            }
            quantiteTotal.setText(qt.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tarifGerantEvent(KeyEvent event) {
        try {
            TableLigneCommande tl = tableLigneCommande.getSelectionModel().getSelectedItem().getValue();
            if (tarifGerant.getText().isEmpty()) {
                balance.setText("Perte : ");
                benefice.setText(tl.prixTotal.get());
            } else {
                Double bal = 0.0, tarifFour = 0.0;
                bal = Double.parseDouble(tarifGerant.getText());
                tarifFour = Double.parseDouble(tl.prixTotal.get());
                bal = bal * Double.parseDouble(tl.quantite.get()) * Double.parseDouble(tl.conditionnement.get());
                if (tarifFour > bal) {
                    balance.setText("Perte : ");
                    benefice.setText(((Double) (tarifFour - bal)).toString());
                } else {
                    balance.setText("Benefice ");
                    benefice.setText(((Double) (bal - tarifFour)).toString());
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @FXML
    private void btnEnregistrerStock(ActionEvent event) {
        if (Notification.confirmDialog("Voulez-vous vraiment enregistrer le stock", "Enregistrement")) {
            try {
                Commande co = commande.getSelectionModel().getSelectedItem();
                HashMap<String, Integer> dis = st.statProduit(co);
                int taille = dis.get("nombreProduitdispo");
                boolean enregistrer = true;
                int i = 0;
                while (i < taille) {
                    TableLigneCommande tl = tableLigneCommande.getSelectionModel().getModelItem(i).getValue();
                    if (Double.parseDouble(tl.tarifGerant.get()) == 0) {
                        Notification.notificationErr("Erreur", "Le prix de vente de certains produits n'a pas ete specifie.");
                        enregistrer = false;
                        i += taille;
                    } else if (Double.parseDouble(tl.prixUnitaire.get()) > Double.parseDouble(tl.tarifGerant.get())) {
                        enregistrer = Notification.confirmDialog("Vous avez indiquer un prix de vente inferieur au prix d'achat Etes-vous sure ?", "Alert");
                        i += taille;
                    }
                    i++;
                }
                if (enregistrer) {
                    try {
                        Stock sto = new Stock();
                        sto.setCodestock(referenceStock.getText());
                        sto.setCategorie(categorie.getText());
                        sto.setQuantitetotal(new BigDecimal(quantiteTotal.getText()).intValueExact());
                        sto.setDatereception(DateManager.asDate(dateReception.getValue()));
                        sto.setPublier(false);
                        stoHome.persist(sto);
                        comHome.update("codestock", sto.getCodestock(), "codecommande", co.getCodecommande());
                        comHome.updateEtat(co, 4);
                        comHome.update("type", sto.getCategorie(), "codecommande", co.getCodecommande());
                        HashMap<String, List<Produit>> rangement = new HashMap<String, List<Produit>>();
                        for (int j = 0; j < taille; j++) {
                            TableLigneCommande tl = tableLigneCommande.getSelectionModel().getModelItem(j).getValue();
                            Famille fam = famHome.findById(tl.codeFamille.get());
                            int qt = Integer.parseInt(tl.quantite.get()) * BigDecimal.valueOf(Double.parseDouble(tl.conditionnement.get())).intValue();
                            List<Produit> ls = new ArrayList();
                            rangement.put(fam.getCodefamille(), ls);
                            for (int k = 0; k < qt; k++) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Produit prod = new Produit();
                                prod.setCodeproduit(GenerateCode.clefUTC("PRO"));
                                prod.setStock(sto);
                                prod.setFamille(fam);
                                prod.setPrixfournisseur(new BigDecimal(tl.prixUnitaire.get()));
                                prod.setDateexpiration(formatter.parse(tl.dateExpiration.get()));
                                prod.setPrixgerant(new BigDecimal(tl.tarifGerant.get()));
                                prod.setRemise(0);
                                boolean persist = prodHome.persist(prod);
                                QRCode.create(prod.getCodeproduit(), prod.getCodeproduit());
                                rangement.get(fam.getCodefamille()).add(prod);
                            }

                        }
                        QRCodePDF.imprimerDevis(rangement, sto.getCodestock());
                        Notification.notificationSuccess("Enregistrement de Stock", "Stock enregistre avec succes!");
                        tableLigneCommande.getRoot().getChildren().clear();

                        initData();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Notification.notificationErr("Enregistrement de Stock", "Stock non enregistre!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void refreshCommande(ActionEvent event) {
        try {
            initComboBox();
        } catch (Exception ex) {
            Logger.getLogger(EnregistrerCommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnAnnuler(ActionEvent event) {
        initData();
    }

    class TableLigneCommande extends RecursiveTreeObject<TableLigneCommande> {

        StringProperty codeFamille;
        StringProperty famille;
        StringProperty quantite;
        StringProperty conditionnement;
        StringProperty tva;
        StringProperty prixUnitaire;
        StringProperty prixTotal;
        StringProperty fournisseur;
        StringProperty categorie;
        StringProperty tarifGerant;
        StringProperty dateReception;
        StringProperty dateExpiration;

        public TableLigneCommande(String codeFamille, String famille, String conditionnement, String quantite, String tva, String prixUnitaire, String prixTotal, String fournisseur) {
            this.codeFamille = new SimpleStringProperty(codeFamille);
            this.famille = new SimpleStringProperty(famille);
            this.conditionnement = new SimpleStringProperty(conditionnement);
            this.quantite = new SimpleStringProperty(quantite);
            this.tva = new SimpleStringProperty(tva);
            this.prixTotal = new SimpleStringProperty(prixTotal);
            this.prixUnitaire = new SimpleStringProperty(prixUnitaire);
            this.fournisseur = new SimpleStringProperty(fournisseur);
            this.categorie = new SimpleStringProperty("Boisson");
            this.tarifGerant = new SimpleStringProperty("0");
            this.dateReception = new SimpleStringProperty(new Date().toString());
            this.dateExpiration = new SimpleStringProperty(new Date().toString());
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

            JFXTreeTableColumn<TableLigneCommande, String> fournisseurcol = new JFXTreeTableColumn<>("Fournisseur");
            fournisseurcol.setPrefWidth(100);
            fournisseurcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().fournisseur;
                }
            });

            JFXTreeTableColumn<TableLigneCommande, String> categoriecol = new JFXTreeTableColumn<>("Categorie");
            categoriecol.setPrefWidth(100);
            categoriecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().categorie;
                }
            });

            JFXTreeTableColumn<TableLigneCommande, String> tarifGerantcol = new JFXTreeTableColumn<>("Prix total");
            tarifGerantcol.setPrefWidth(100);
            tarifGerantcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().tarifGerant;
                }
            });

            JFXTreeTableColumn<TableLigneCommande, String> dateReceptioncol = new JFXTreeTableColumn<>("Prix total");
            dateReceptioncol.setPrefWidth(100);
            dateReceptioncol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().dateReception;
                }
            });

            JFXTreeTableColumn<TableLigneCommande, String> dateExpirationcol = new JFXTreeTableColumn<>("Prix total");
            dateExpirationcol.setPrefWidth(100);
            dateExpirationcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableLigneCommande, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableLigneCommande, String> param) {
                    return param.getValue().getValue().dateExpiration;
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
    List<Fournisseur> listFournisseur = new ArrayList<>();
    ObservableList obslistFournisseur;
    List<Commande> listCommande = new ArrayList<>();
    ObservableList obslistCommande;

    private void initComboBox() {

        try {
            listCommande = comHome.commandeValideList();
        } catch (RemoteException ex) {
            Logger.getLogger(EnregistrerCommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        commande.setButtonCell(new SimpleCommandeCell());
        obslistCommande = FXCollections.observableList(listCommande);
        commande.setItems(obslistCommande);
        commande.setCellFactory(listView -> new SimpleCommandeCell());

    }

    public void initData() {
        paneVisualisation.setVisible(false);
        aucunElementSelect.setVisible(true);
        referenceStock.setText("");
        categorie.setText("");
        quantiteTotal.setText("");
        categorie.setText("");
        fournisseur.setText("");
        tarifGerant.setText("");
        balance.setText("Benefice : ");
        benefice.setText("");

    }

    public class SimpleFournisseurCell extends ListCell<Fournisseur> {

        @Override
        protected void updateItem(Fournisseur item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (!empty && item != null) {
                final String text = String.format("%s %s", item.getPersonne().getPrenom(), item.getPersonne().getNom());
                setText(text);
            }
        }

    }
}
