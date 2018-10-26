/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.treluxcom.metier.Client;
import com.treluxcom.metier.Personne;
import com.treluxcom.service.IClientHome;
import com.treluxcom.service.IPersonneHome;
import com.treluxcom.utilitaire.FileManager;
import com.treluxcom.utilitaire.GenerateCode;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.WindowManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.persistence.NoResultException;

/**
 * FXML Controller class
 *
 * @author ADA-MALICK
 */
public class FormulaireClientController implements Initializable {

    @FXML
    private JFXTextField prenom;
    @FXML
    private JFXTextField nom;
    @FXML
    private JFXTextField telephone;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXPasswordField motpasse1;
    @FXML
    private JFXPasswordField motpasse2;
    @FXML
    private DatePicker datenaissance;
    private JFXTextField lieunaissance;
    @FXML
    private JFXTextField nationalite;
    @FXML
    private JFXTextField adresse;
    @FXML
    private ImageView profil;

    @FXML
    ToggleGroup sexe;
    @FXML
    private Label reponse;

    String sexeText;
    ApplicationContext context;
    IClientHome clientHome;
    Client client;
    Personne personne;
    FileManager filemanager;

    @FXML
    private JFXTextField pays;
    @FXML
    private ImageView reponseImg;
    @FXML
    private JFXRadioButton m;
    @FXML
    private JFXRadioButton f;
    @FXML
    private JFXTextField login;
    IPersonneHome personneHome;
    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView imageProfil;

    private double xOffset = 0;
    private double yOffset = 0;

    public FormulaireClientController() {
        this.sexeText = "m";

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
                PageInternauteController.stageIns.setX(event.getScreenX() - xOffset);
                PageInternauteController.stageIns.setY(event.getScreenY() - yOffset);
                PageInternauteController.stageIns.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            PageInternauteController.stageIns.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            PageInternauteController.stageIns.setOpacity(1.0f);
        });

    }

    String nameProfil;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameProfil = GenerateCode.clefUTC("IMG");
        makeStageDrageable();
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
            filemanager = context.getBean("filemanager", FileManager.class);
            personne = context.getBean("personne", Personne.class);
            personne.setPhoto("user.png");
            client = context.getBean("client", Client.class);
            clientHome = (IClientHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiClientHome");
            personneHome = (IPersonneHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPersonneHome");

            telephone.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,10}([\\+]\\d{0})?")) {
                        telephone.setText(oldValue);
                    }
                }
            });

        } catch (Exception ex) {
        }
    }

    @FXML
    private void btnInscrire(ActionEvent event) {
        if (existenceEmail) {
            reponse.setText("E-mail existant!");
        } else if (existenceLogin) {
            reponse.setText("Login existant!");
        } else if (motpasse1.getText().isEmpty() || !motpasse1.getText().equals(motpasse2.getText())) {
            motpasse1.setText("");
            motpasse2.setText("");
            motpasse2.setVisible(false);
            reponse.setText("Mot de passe incorrect!");
        } else if (prenom.getText().isEmpty()) {
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
            reponse.setText("Champ 'Telephone' obligatoire!");
            reponseImg.setVisible(true);
        } else if (motpasse1.getText().isEmpty()) {
            reponse.setText("Champ 'Mot de passe' obligatoire!");
            reponseImg.setVisible(true);
        } else if (datenaissance.getValue() == null) {
            reponse.setText("Champ 'date de naissance' obligatoire!");
        } else {
            try {
                personne.setDatenaiss(datenaissance.getValue().toString());

                personne.setProfil("Client");
                personne.setSession(false);
                personne.setBloque(0);
                personne.setNombreconnexion(0);
                personne.setCodeaccess(0);
                personne.setDroit("connect");
                personne.setLieunaiss("Non precise");
                personne.setCodepersonne(GenerateCode.clefUTC("CLI"));
                personne.setPrenom(prenom.getText());
                personne.setNom(nom.getText());
                personne.setEmail(email.getText());
                personne.setLogin(login.getText());
                personne.setAdresse(adresse.getText());
                personne.setTelephone(telephone.getText());
                personne.setSexe(sexeText);
                personne.setNationalite(nationalite.getText());
                personne.setMotpasse(motpasse1.getText());
                personne.setPhoto(FINAL_NAME);
                client.setPersonne(personne);
                clientHome.persist(client);

                try {
                    FileManager.copier(new File(PATH_TMP + FINAL_NAME), new File("src/main/resources/images/profil/" + FINAL_NAME));
                    FileManager.deleteDirectoryWithContains(new File(PATH_TMP));
                } catch (IOException iOException) {
                }

                new WindowManager().switchScene("Connexion", "/fxml/Connexion.fxml", event);
            } catch (NullPointerException e) {
                e.printStackTrace();
                //reponseImg.setVisible(true);
            } catch (RemoteException ex) {
                Logger.getLogger(FormulaireClientController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FormulaireClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void eventMotpasse1(ActionEvent event) {
        if (motpasse1.getText().length() != 0) {
            motpasse2.setVisible(true);
        } else {
            motpasse2.setText("");
        }
    }

    private void onKeyRealeasedMotpasse2(ActionEvent event) {
        if (!motpasse1.getText().equals(motpasse2.getText())) {
            reponse.setText("Mots de passse non identiques");
            reponseImg.setVisible(true);
        }
    }

    @FXML
    private void eventRadio(ActionEvent e) {

        if (m.isSelected()) {
            sexeText = m.getText();
        }
        if (f.isSelected()) {
            sexeText = f.getText();
        }

    }

    @FXML
    private void onKeyPressedMotpass1(KeyEvent event) {
        if (!motpasse1.getText().isEmpty()) {
            motpasse2.setVisible(true);
        } else {
            motpasse2.setVisible(false);
        }
    }

    String PATH_TMP;
    String FINAL_NAME = "user.png";
    boolean existenceEmail = false, existenceLogin = false;

    @FXML
    private void btnChargerPhoto(ActionEvent event) throws MalformedURLException, IOException {
        PATH_TMP = "src/main/resources/images/tmp/" + nameProfil + "/";
        FileManager.creer(PATH_TMP + "/img");

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            String imagepath = "file:///" + file.getPath();
            FINAL_NAME = nameProfil + FileManager.getExtension(file);
            URL imageURL = new URL(imagepath);
            BufferedImage bi = ImageIO.read(imageURL);

            ImageIO.write(bi, "jpg", new File(PATH_TMP + FINAL_NAME));

            System.out.println("Copied!");

            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(new File(PATH_TMP + FINAL_NAME));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            this.imageProfil.setImage(image);

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please Select a File");
            /*alert.setContentText("You didn't select a file!");*/
            alert.showAndWait();
        }
    }

    @FXML
    private void onKeyRealeasedEmail(KeyEvent event) {
        if (!email.getText().isEmpty()) {
            try {
                if (!personneHome.findByEmail(email.getText()).getCodepersonne().isEmpty()) {
                    reponse.setText("E-mail existant!");
                    reponseImg.setVisible(true);
                    existenceEmail = true;
                }
            } catch (NoResultException err) {
                //Email valide
                reponse.setText("");
                reponseImg.setVisible(false);
                existenceEmail = false;
            } catch (RemoteException ex) {
                Logger.getLogger(FormulaireClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            email.setText("");
            reponse.setText("");
            reponseImg.setVisible(false);
        }
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {
    }

    @FXML
    private void btnAnnuler(ActionEvent event) {
        prenom.setText("");
        nom.setText("");
        m.setSelected(true);
        adresse.setText("");
        telephone.setText("");
        pays.setText("");
        nationalite.setText("");
        email.setText("");
        login.setText("");
        existenceLogin = false;
        existenceEmail = false;
    }

    @FXML
    private void btnExit(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private void loginEvent(KeyEvent event) {
        if (!login.getText().isEmpty()) {
            try {
                if (!personneHome.findByLogin(login.getText()).getCodepersonne().isEmpty()) {
                    reponse.setText("Login existant!");
                    reponseImg.setVisible(true);
                    existenceLogin = true;
                }
            } catch (NoResultException err) {
                //Email valide
                reponse.setText("");
                reponseImg.setVisible(false);
                existenceLogin = false;
            } catch (RemoteException ex) {
                Logger.getLogger(FormulaireClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            login.setText("");
            reponse.setText("");
            reponseImg.setVisible(false);
        }
    }

}
