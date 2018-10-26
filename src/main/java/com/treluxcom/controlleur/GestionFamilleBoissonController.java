package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Ressourcemedia;
import com.treluxcom.service.IFamilleHome;
import com.treluxcom.utilitaire.FileManager;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import org.hibernate.HibernateException;

public class GestionFamilleBoissonController implements Initializable {

    @FXML
    private AnchorPane gestionFamille;

    @FXML
    private Pane ajoutFamille;

    @FXML
    private JFXToggleButton minerale;

    @FXML
    private JFXToggleButton gazeuse;

    @FXML
    private JFXToggleButton naturelle;

    @FXML
    private JFXToggleButton sucree;

    @FXML
    private JFXToggleButton alcoolisee;

    private TextField nomBoutique;

    @FXML
    private TextField degreAlcool;

    @FXML
    private ComboBox<String> saveur;
    ObservableList<String> listSaveur = FXCollections.observableArrayList();

    @FXML
    private TextArea description;

    @FXML
    private JFXTreeTableView<TableFamille> tableFamille;

    Famille fam = new Famille();
    IFamilleHome famHome;
    @FXML
    private TextField format;
    @FXML
    private TextField nomFamille;
    @FXML
    private ComboBox<String> unite;
    ObservableList<String> listUnite = FXCollections.observableArrayList();
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
    private JFXTextField recherche;
    @FXML
    private Pane paneVisualisation;
    @FXML
    private ImageView photoFamille;

    @FXML
    private void mnSupprimer(ActionEvent event) throws RemoteException {
        TableFamille te = tableFamille.getSelectionModel().getSelectedItem().getValue();
        try {
            if (Notification.confirmDialog("Voulez-vous supprimer la famille de boisson " + te.famille.get(), "Suppression")) {
                famHome.deleteById(te.codeFamille.get());
                tableFamille.getSelectionModel().getSelectedItem().getParent().getChildren().remove(tableFamille.getSelectionModel().getSelectedItem());
                Notification.notificationSuccess("Suppression", "La famille de boisson a été supprimée avec succès");
            }
        } catch (HibernateException err) {
            Notification.notificationErr("Suppression", "Echoué");
            err.printStackTrace();
        }

    }

    @FXML
    void btnEnregistrerFamille(ActionEvent event) {
        try {
            if (nomFamille.getText().isEmpty()) {
                Notification.notificationErr("Erreur", "le Champ 'Nom' est obligatoire");
            } else if (format.getText().isEmpty()) {
                Notification.notificationErr("Erreur", "le Champ 'Format' est obligatoire");
            } else if (degreAlcool.getText().isEmpty()) {
                Notification.notificationErr("Erreur", "le Champ 'Degré d'alcool' est obligatoire");
            } else {
                if (Notification.confirmDialog("Veuillez confirmer l'ajout de la famille de boisson " + nomFamille.getText(), "Enregistrement")) {
                    fam.setCodefamille(GenerateCode.clefUTC("FAM"));
                    fam.setNom(nomFamille.getText());
                    fam.setAlcoolise(alcoolisee.isSelected());
                    fam.setDegresalcool(BigDecimal.valueOf(Double.parseDouble((degreAlcool.getText()))));
                    fam.setGazeuse(gazeuse.isSelected());
                    fam.setFormat(format.getText() + " " + unite.getSelectionModel().getSelectedItem());
                    fam.setLibelle(description.getText());
                    fam.setMineral(minerale.isSelected());
                    fam.setNaturel(naturelle.isSelected());
                    fam.setSucre(sucree.isSelected());
                    fam.setQuantite(0);
                    fam.setSaveur(saveur.getSelectionModel().getSelectedItem());
                    famHome.persist(fam);

                    tableFamilleList.add(new TableFamille(
                            fam.getNom(),
                            fam.getFormat(),
                            fam.getSaveur(),
                            fam.getSucre().toString(),
                            fam.getNaturel().toString(),
                            fam.getGazeuse().toString(),
                            fam.getAlcoolise().toString(),
                            fam.getDegresalcool().toString(),
                            fam.getMineral().toString(),
                            fam.getCodefamille(),
                            fam.getLibelle()
                    )
                    );
                    try {
                        FileManager.copier(new File(PATH_TMP + FINAL_NAME), new File("src/main/resources/images/ressourceMedia/" + FINAL_NAME));
                        FileManager.deleteDirectoryWithContains(new File(PATH_TMP));
                    } catch (IOException iOException) {
                    }
                    Notification.notificationSuccess("Insertion ", "Famille de boisson enregistrée avec succès");
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void initCombo() {
        listUnite.addAll("Cl", "l", "m3");
        listSaveur.addAll("Acre", "Limonade", "Juteuse");
        unite.setItems(listUnite);
        saveur.setItems(listSaveur);
        unite.getSelectionModel().select(0);
        saveur.getSelectionModel().select(0);
    }

    String FINAL_NAME;
    String PATH_TMP;
    String nameProfil = "";

    @FXML
    void chargerPhoto(ActionEvent event) {
        try {
           Notification.notificationErr("Build", "Button pas encore configure");
        } catch (Exception e) {
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            format.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,7}([\\.]\\d{0})?")) {
                        format.setText(oldValue);
                    }
                }
            });
            famHome = (IFamilleHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiFamilleHome");
            initCombo();
            initTableviewFamille();
            recherche.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    tableFamille.setPredicate(new Predicate<TreeItem<TableFamille>>() {
                        @Override
                        public boolean test(TreeItem<TableFamille> t) {
                            Boolean flag = t.getValue().famille.getValue().contains(newValue) || t.getValue().saveur.getValue().contains(newValue);
                            return flag;
                        }
                    }
                    );
                }
            });
            tableFamille.setOnMouseClicked((e) -> {
                paneVisualisation.setVisible(true);
                TableFamille tf = tableFamille.getSelectionModel().getSelectedItem().getValue();
                lbSaveur.setText("- " + tf.saveur.get());
                lbFormat.setText("- Format: " + tf.format.get());
                lbNomFamille.setText(tf.famille.get());
                lbDescription.setText(tf.description.get());
                lbDegreAlcool.setText("- Degré d'alcool: " + tf.degreAlcool.get());
                if (tf.sucre.get().equals("true")) {
                    lbSucree.setText("- Sucrée");
                } else {
                    lbSucree.setText("- Non Sucrée");
                }
                if (tf.naturel.get().equals("true")) {
                    lbNaturelle.setText("- Naturelle");
                } else {
                    lbNaturelle.setText("- Non Naturelle");
                }
                if (tf.mineral.get().equals("true")) {
                    lbMinerale.setText("- Minérale");
                } else {
                    lbMinerale.setText("- Non Minérale");
                }
                if (tf.alcool.get().equals("true")) {
                    lbAlcoolisee.setText("- Alcoolisée");
                } else {
                    lbAlcoolisee.setText("- Non Alcoolisée");
                }
                if (tf.gazeuse.get().equals("true")) {
                    lbGazeuse.setText("- Gazeuse");
                } else {
                    lbGazeuse.setText("- Non Gazeuse");
                }
                try {
                    Ressourcemedia res = (Ressourcemedia) new ArrayList(famHome.findById(tf.getCodeFamille()).getRessourcemedias()).get(0);
                    BufferedImage bufferedImage;
                    bufferedImage = ImageIO.read(new File("src/main/resources/images/ressourceMedia/" + res.getLocalisation()));
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    this.photoFamille.setImage(image);
                } catch (Exception ex) {
                    try {
                        BufferedImage bufferedImage;
                        bufferedImage = ImageIO.read(new File("src/main/resources/images/ressourceMedia/defaut.png"));
                        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                        this.photoFamille.setImage(image);
                    } catch (Exception err) {
                    }
                }

            });
            tableFamille.setOnKeyReleased((e) -> {
                paneVisualisation.setVisible(true);
                TableFamille tf = tableFamille.getSelectionModel().getSelectedItem().getValue();
                lbSaveur.setText("- " + tf.saveur.get());
                lbFormat.setText("- Format: " + tf.format.get());
                lbNomFamille.setText(tf.famille.get());
                lbDescription.setText(tf.description.get());
                lbDegreAlcool.setText("- Degré d'alcool: " + tf.degreAlcool.get());
                if (tf.sucre.get().equals("true")) {
                    lbSucree.setText("- Sucrée");
                } else {
                    lbSucree.setText("- Non Sucrée");
                }
                if (tf.naturel.get().equals("true")) {
                    lbNaturelle.setText("- Naturelle");
                } else {
                    lbNaturelle.setText("- Non Naturelle");
                }
                if (tf.mineral.get().equals("true")) {
                    lbMinerale.setText("- Minérale");
                } else {
                    lbMinerale.setText("- Non Minérale");
                }
                if (tf.alcool.get().equals("true")) {
                    lbAlcoolisee.setText("- Alcoolisée");
                } else {
                    lbAlcoolisee.setText("- Non Alcoolisée");
                }
                if (tf.gazeuse.get().equals("true")) {
                    lbGazeuse.setText("- Gazeuse");
                } else {
                    lbGazeuse.setText("- Non Gazeuse");
                }
                try {
                    Ressourcemedia res = (Ressourcemedia) new ArrayList(famHome.findById(tf.getCodeFamille()).getRessourcemedias()).get(0);
                    BufferedImage bufferedImage;
                    bufferedImage = ImageIO.read(new File("src/main/resources/images/ressourceMedia/" + res.getLocalisation()));
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    this.photoFamille.setImage(image);
                } catch (Exception ex) {
                    try {
                        BufferedImage bufferedImage;
                        bufferedImage = ImageIO.read(new File("src/main/resources/images/ressourceMedia/defaut.png"));
                        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                        this.photoFamille.setImage(image);
                    } catch (Exception err) {
                    }
                }
            });

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    class TableFamille extends RecursiveTreeObject<TableFamille> {

        StringProperty famille;
        StringProperty saveur;
        StringProperty format;
        StringProperty sucre;
        StringProperty naturel;
        StringProperty gazeuse;
        StringProperty mineral;
        StringProperty alcool;
        StringProperty degreAlcool;
        StringProperty codeFamille;
        StringProperty description;

        private String getCodeFamille() {
            return this.codeFamille.get();
        }

        public TableFamille(String famille, String format, String saveur, String sucre, String naturel, String gazeuse, String alcool, String degreAlcool, String mineral, String codeFamille, String description) {
            this.famille = new SimpleStringProperty(famille);
            this.format = new SimpleStringProperty(format);
            this.saveur = new SimpleStringProperty(saveur);
            this.sucre = new SimpleStringProperty(sucre);
            this.gazeuse = new SimpleStringProperty(gazeuse);
            this.mineral = new SimpleStringProperty(mineral);
            this.alcool = new SimpleStringProperty(alcool);
            this.naturel = new SimpleStringProperty(naturel);
            this.degreAlcool = new SimpleStringProperty(degreAlcool);
            this.description = new SimpleStringProperty(description);
            this.codeFamille = new SimpleStringProperty(codeFamille);
        }
    }
    ObservableList<TableFamille> tableFamilleList = FXCollections.observableArrayList();

    public void initTableviewFamille() {

        try {
            JFXTreeTableColumn<TableFamille, String> famillecol = new JFXTreeTableColumn<>("Famille");
            famillecol.setPrefWidth(180);
            famillecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().famille;
                }
            });
            JFXTreeTableColumn<TableFamille, String> formatcol = new JFXTreeTableColumn<>("Format");
            formatcol.setPrefWidth(130);
            formatcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().format;
                }
            });

            JFXTreeTableColumn<TableFamille, String> saveurcol = new JFXTreeTableColumn<>("Saveur");
            saveurcol.setPrefWidth(150);
            saveurcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().saveur;
                }
            });

            JFXTreeTableColumn<TableFamille, String> sucrecol = new JFXTreeTableColumn<>("Sucrée");
            sucrecol.setPrefWidth(100);
            sucrecol.setSortable(true);
            sucrecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().sucre;
                }
            });
            JFXTreeTableColumn<TableFamille, String> naturelcol = new JFXTreeTableColumn<>("Naturel");
            naturelcol.setPrefWidth(100);
            naturelcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().naturel;
                }
            });
            JFXTreeTableColumn<TableFamille, String> codeFamillecol = new JFXTreeTableColumn<>("Naturel");
            codeFamillecol.setPrefWidth(100);
            codeFamillecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().codeFamille;
                }
            });
            JFXTreeTableColumn<TableFamille, String> gazeusecol = new JFXTreeTableColumn<>("Gazeuse");
            gazeusecol.setPrefWidth(100);
            gazeusecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().gazeuse;
                }
            });
            JFXTreeTableColumn<TableFamille, String> alcoolcol = new JFXTreeTableColumn<>("Alcoolisée");
            alcoolcol.setPrefWidth(100);
            alcoolcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().alcool;
                }
            });
            JFXTreeTableColumn<TableFamille, String> degrecol = new JFXTreeTableColumn<>("Degre Alcool");
            degrecol.setPrefWidth(100);
            degrecol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().degreAlcool;
                }
            });
            JFXTreeTableColumn<TableFamille, String> mineralcol = new JFXTreeTableColumn<>("Minérale");
            mineralcol.setPrefWidth(100);
            mineralcol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().mineral;
                }
            });
            JFXTreeTableColumn<TableFamille, String> descriptioncol = new JFXTreeTableColumn<>("Description");
            descriptioncol.setPrefWidth(100);
            descriptioncol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableFamille, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableFamille, String> param) {
                    return param.getValue().getValue().description;
                }
            });

            List<Famille> familleRefresh = famHome.familleList();
            Iterator familles = familleRefresh.iterator();
            while (familles.hasNext()) {
                Famille fam = (Famille) familles.next();
                tableFamilleList.add(new TableFamille(
                        fam.getNom(),
                        fam.getFormat(),
                        fam.getSaveur(),
                        fam.getSucre().toString(),
                        fam.getNaturel().toString(),
                        fam.getGazeuse().toString(),
                        fam.getAlcoolise().toString(),
                        fam.getDegresalcool().toString(),
                        fam.getMineral().toString(),
                        fam.getCodefamille(),
                        fam.getLibelle())
                );
            }
            TreeItem<TableFamille> root1 = new RecursiveTreeItem<TableFamille>(tableFamilleList, RecursiveTreeObject::getChildren);
            tableFamille.getColumns().setAll(famillecol, formatcol, saveurcol, degrecol, sucrecol, naturelcol);
            tableFamille.setRoot(root1);
            tableFamille.setShowRoot(false);
            tableFamille.getSelectionModel().select(0);

        } catch (Exception err) {
            Notification.notificationErr("Erreur", "Problème lors du chargement des données");
        }

    }

}
