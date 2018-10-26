package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXButton;
import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Ressourcemedia;
import com.treluxcom.service.IFamilleHome;
import com.treluxcom.service.IProduitHome;
import com.treluxcom.service.IStockHome;
import com.treluxcom.utilitaire.Notification;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.WindowManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PageInternauteController implements Initializable {

    @FXML
    private ScrollPane scrollPaneClient;

    ApplicationContext context;
    WindowManager wm;
    @FXML
    private Label label;
    @FXML
    private Pagination pagination;
    @FXML
    private AnchorPane anchor = new AnchorPane();

    private static String sep = File.separator;
    private static String PATH = "." + sep + "src" + sep + "main" + sep + "resources" + sep + "images" + sep + "slide";
    @FXML
    private StackPane stackpane;
    @FXML
    private VBox vboxProduit;
    IProduitHome prodHome;
    IStockHome stoHome;
    IFamilleHome famHome;
    @FXML
    private MediaView media;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            context = new ClassPathXmlApplicationContext("spring.xml");

            wm = context.getBean("windowmanager", WindowManager.class);
            mediaplayer();
            File selectedDirectory = new File(PATH);

            if (selectedDirectory != null) {
                FilenameFilter filterJpg = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".jpg");
                    }
                };

                filesJpg = selectedDirectory.listFiles(filterJpg);

            }
            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    return createPage(pageIndex);
                }

            });
        } catch (BeansException beansException) {
        }
        try {
            stoHome = (IStockHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiStockHome");
            famHome = (IFamilleHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiFamilleHome");
            prodHome = (IProduitHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiProduitHome");
            affichageProduit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    static Stage stageCo = null;

    public void stopMedia() {
        mediaPlayer.stop();
    }

    @FXML
    private void btnConnexion(ActionEvent event) throws IOException {
        try {

            Stage stageBisCo = new Stage();
            FXMLLoader chargeurBisCo = new FXMLLoader(getClass().getResource("/fxml/Connexion.fxml"));
            Parent rootBisCo = (Parent) chargeurBisCo.load();
            Scene sceneBisCo = new Scene(rootBisCo);
            ConnexionController connexionController = chargeurBisCo.getController();

            connexionController.setPageprecedente((Stage) this.anchor.getScene().getWindow());
            connexionController.setPic(this);
            this.stageCo = stageBisCo;
            stageBisCo.setScene(sceneBisCo);
            stageBisCo.initStyle(StageStyle.UNDECORATED);
            stageBisCo.show();

        } catch (Exception iOException) {
        }
    }

    static Stage stageIns = null;

    @FXML
    private void btnSinscrire(ActionEvent event) throws IOException {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/InscriptionClient.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            this.stageIns = primaryStage;
            primaryStage.show();
        } catch (Exception exception) {
        }
    }

    File filesJpg[];

    public VBox createPage(int index) {

        ImageView imageView = new ImageView();
        File file = filesJpg[index];
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
            imageView.setFitWidth(840);
            imageView.setFitHeight(400);
            // imageView.setPreserveRatio(true);

            imageView.setSmooth(true);
            imageView.setCache(true);
        } catch (IOException ex) {

        }

        VBox pageBox = new VBox();
        pageBox.getChildren().add(imageView);

        return pageBox;
    }

    private void openDirectoryChooser(Stage parent) {

        File selectedDirectory = new File(PATH);

        if (selectedDirectory != null) {
            FilenameFilter filterJpg = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jpg");
                }
            };

            filesJpg = selectedDirectory.listFiles(filterJpg);

        }
    }

    @FXML
    private void btnEvent1(ActionEvent event) {
        try {
            Notification.alert("Evenement", stackpane, "Le tour du Sénégal est une épreuve internationale de cyclisme par étapes. Pendant 8 jours, "
                    + "elle accueille des coureurs d’Europe et d’Afrique."
                    + "\nPour cette 16éme édition, la Trelux Distribustion a accompagné le tour du Sénégal en sponsorisant le maillot vert. \n"
                    + "De Dakar à Saint-Louis, en passant par Thiès et Kaolack, nous avons parcouru un total de 1082 km aux couleurs de XXL. \n"
                    + "Composé en 8 étapes dont deux circuits fermés, le tour du Sénégal était accompagné de l’équipe XXL de la Trelux Distribustion,"
                    + "\nqui a partagé son énergie débordante avec les coureurs, leurs équipes et aussi les supporteurs. \n"
                    + "Hôtesses, acrobates et danseurs, tous étaient présents à chaque étape pour booster le tonus des équipes et surtout des coureurs. "
                    + ""
            );
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnEvent2(ActionEvent event) {
        try {
            Notification.alert("Evenement", stackpane, "Les 15 et 16 Avril 2017, le Sénégal a accueilli son premier marathon national, le marathon de Dakar."
                    + " Un événement sportif qui a réuni athlètes, passionnés de sport et humanistes.\n"
                    + "La Trelux Distribustion, partenaire et présente au village du Marathon de Dakar, invitait les coureurs à faire le plein d’énergie avec XXL.\n"
                    + "\n"
                    + "Entre animations et discussions nous avons eu un fort intérêt auprès du Ministre des Sports, Son Excellence Monsieur Matar BA.\n"
                    + "\n"
                    + "Merci aux coureurs de la Trelux Distribustion qui ont montré qu’avec XXL, ils étaient puissants."
            );
        } catch (Exception e) {
        }
    }

    @FXML
    private void btnEvent3(ActionEvent event) {
        try {
            Notification.alert("Evenement", stackpane, "Pétillante et rafraîchissante, La Gazelle est une marque de tradition de la Trelux Distribustion qui partage"
                    + "les valeurs d’authenticité de génération en génération."
                    + "\n"
                    + "Soucieuse de ses consommateurs et adeptes de la marque, la boisson gazeuse renforce sa gamme et change de look avec un nouveau packaging 125 cl.\n"
                    + "\n"
                    + "La famille Gazelle s’agrandit et présente son nouveau parfum, la Gazelle Orange."
            );
        } catch (Exception e) {
        }
    }

    static Stage stage = null;

    @FXML
    private void btnConfig(ActionEvent event) throws IOException {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ServerConfiguration.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            this.stage = primaryStage;
            primaryStage.show();
        } catch (Exception exception) {
        }
    }

    private static String MEDIA_URL = "publicite.MPEG4";
    private MediaPlayer mediaPlayer;

    private void mediaplayer() {
        try {
            mediaPlayer = new MediaPlayer(new Media(new File("publicite.mp4").toURI().toString()));
            mediaPlayer.setAutoPlay(true);
            media.setFitWidth(385);
            media.setFitHeight(500);
            ///    Resize Hack
//        DoubleProperty mvw = media.fitWidthProperty();
//        DoubleProperty mvh = media.fitHeightProperty();
//        mvw.bind(Bindings.selectDouble(media.sceneProperty(), "width"));
//        mvh.bind(Bindings.selectDouble(media.sceneProperty(), "height"));

            media.setMediaPlayer(mediaPlayer);
        } catch (Exception e) {
        }
    }

    private void affichageProduit() {
        try {
            String colorPane = "skyblue";
            HashMap<String, Integer> produits = stoHome.famillePublie(stoHome.stockPublieList());
            produits.remove(0);

            List<String> familleCode = new ArrayList(produits.keySet());
            int nbreHbox = (produits.size() / 6) + 1;
            if (nbreHbox == 1) {
                vboxProduit.setPrefHeight(195);
                vboxProduit.setPrefWidth(840);
            } else {
                vboxProduit.setPrefHeight(nbreHbox * 180);
            }
            int j = 0;
            for (int i = 0; i < nbreHbox; i++) {

                HBox hBox = new HBox();
                hBox.setPrefWidth(200);
                hBox.setPrefHeight(100);
                hBox.setSpacing(24);

                try {
                    for (int k = 0; k < 6; k++) {
                        Famille fam = famHome.findById(familleCode.get(j));
                        AnchorPane plaque = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/PlaqueProduit.fxml"));
                        plaque.setBackground(Background.EMPTY);
                        String style = "-fx-background-color:" + colorPane + ";";
                        plaque.setStyle(style);

                        String ressourceName = null;
                        try {
                            ressourceName = ((Ressourcemedia) new ArrayList(fam.getRessourcemedias()).get(0)).getLocalisation();
                            ((ImageView) (plaque.getChildren().get(0))).setImage(new Image("/images/ressourceMedia/" + ressourceName));
                        } catch (Exception e) {
                        }
                        Double price =prodHome.getPrice(fam).doubleValue();                       
                        ((Label) (plaque.getChildren().get(1))).setText(produits.get(familleCode.get(j)).toString());
                        ((Label) (plaque.getChildren().get(2))).setText(price.toString() + " FCFA");
                        ((JFXButton) (plaque.getChildren().get(3))).setOnAction((e) -> {
                            Notification.infoAlert("Veuillez-vous connecter pour pouvoir acheter ce produit", "Informations");

                        });

                        hBox.getChildren().add(plaque);
                        j++;
                    }
                } catch (Exception ex) {
                 
                }
                vboxProduit.getChildren().add(hBox);

            }
        } catch (Exception ex) {

        }
    }
}
