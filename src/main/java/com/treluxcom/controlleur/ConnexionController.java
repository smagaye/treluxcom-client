package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.treluxcom.metier.Personne;
import com.treluxcom.service.IPersonneHome;
import com.treluxcom.utilitaire.FileManager;
import com.treluxcom.utilitaire.Reseau;
import com.treluxcom.utilitaire.WindowManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.persistence.NoResultException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConnexionController implements Initializable {

    @FXML
    private JFXTextField login;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label reponse;
    private Personne personne;
    ApplicationContext contexte;
    WindowManager wm;
    IPersonneHome iph;
    public static Stage stageBack = new Stage();
    @FXML
    private AnchorPane parent;

    private double xOffset = 0;
    private double yOffset = 0;
    private Stage pageprecedente;

    public void setPageprecedente(Stage pageprecedente) {
        this.pageprecedente = pageprecedente;
    }

    private PageInternauteController pic;

    public void setPic(PageInternauteController pic) {
        this.pic = pic;
    }

    private void makeStageDrageable() {
        try {
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
                    PageInternauteController.stageCo.setX(event.getScreenX() - xOffset);
                    PageInternauteController.stageCo.setY(event.getScreenY() - yOffset);
                    PageInternauteController.stageCo.setOpacity(0.7f);
                }
            });
            parent.setOnDragDone((e) -> {
                PageInternauteController.stageCo.setOpacity(1.0f);
            });
            parent.setOnMouseReleased((e) -> {
                PageInternauteController.stageCo.setOpacity(1.0f);
            });
        } catch (Exception e) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            makeStageDrageable();
            iph = (IPersonneHome) java.rmi.Naming.lookup("rmi://" + Reseau.getAdresseip() + ":" + Reseau.getPort() + "/rmiPersonneHome");

        } catch (Exception ex) {
            Logger.getLogger(ConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        contexte = new ClassPathXmlApplicationContext("spring.xml");
        FileManager.logErreur(FileManager.getLog(), "Succes", "Démarrage Spring");
        FileManager.logErreur(FileManager.getLog(), "Succes", "Démarrage Hibernate");
        wm = contexte.getBean("windowmanager", WindowManager.class);
        personne = contexte.getBean("personne", Personne.class);
    }

    @FXML
    private void btnConnexion(ActionEvent e) throws IOException {
        try {
            String log = login.getText();
            String pass = password.getText();
            if (log.isEmpty()) {
                reponse.setText("Nom d'utilisateur Obligatoire");
            } else if (pass.isEmpty()) {
                reponse.setText("Mot de passe Obligatoire");
            } else {
                try {
                    personne = iph.connection(log, pass);
                    int ndreCo = personne.getNombreconnexion().intValue() + 1;
                    if (personne.getProfil().equals("")) {
                        reponse.setText("Nom d'utilisateur ou mot de passe incorrect!");
                    } else if (personne.getSession()) {
                        //NullPointerException si
                    } else if ("Client".equals(personne.getProfil())) {
                        iph.update("nombreconnexion", Integer.toString(ndreCo), "codepersonne", personne.getCodepersonne());
                        Stage stageBisCli = new Stage();
                        FXMLLoader chargeurBisCli = new FXMLLoader(getClass().getResource("/fxml/MainClient.fxml"));
                        Parent rootBisCli = (Parent) chargeurBisCli.load();
                        Scene sceneBisCli = new Scene(rootBisCli);
                        MainClientController mainClientController = chargeurBisCli.getController();
                        mainClientController.setPersonne(personne);
                        this.stageBack = stageBisCli;
                        stageBisCli.setScene(sceneBisCli);
                        stageBisCli.show();

                        pic.stopMedia();
                        WindowManager.closeWindow(e);
                        WindowManager.closeWindow(pageprecedente);
                    } else if ("admin".equals(personne.getProfil())) {
                        iph.update("nombreconnexion", Integer.toString(ndreCo), "codepersonne", personne.getCodepersonne());
                        Stage primaryStage = new Stage();
                        FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fxml/Administrateur.fxml"));
                        Parent root = (Parent) chargeur.load();
                        Scene scene = new Scene(root);
                        AdministrateurController mainAdminController = chargeur.getController();
                        mainAdminController.setAdmin(personne);

                        primaryStage.setTitle("Administrateur");
                        primaryStage.setScene(scene);
                        primaryStage.show();

                        pic.stopMedia();
                        WindowManager.closeWindow(e);
                        WindowManager.closeWindow(pageprecedente);

                    } else if ("fournisseur".equals(personne.getProfil())) {
                        iph.update("nombreconnexion", Integer.toString(ndreCo), "codepersonne", personne.getCodepersonne());
                        Stage stage = new Stage();
                        FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fxml/MainFournisseur.fxml"));
                        Parent root = (Parent) chargeur.load();
                        Scene scene = new Scene(root);
                        MainFournisseurController mainFournisseurController = chargeur.getController();
                        mainFournisseurController.setPersonne(personne);

                        stage.setTitle("Fournisseur");
                        stage.setScene(scene);
                        stage.show();

                        pic.stopMedia();
                        WindowManager.closeWindow(e);
                        WindowManager.closeWindow(pageprecedente);
                    } else if ("employe".equals(personne.getProfil())) {
                        switch (personne.getEmploye().getFonction()) {
                            case "Caissier":
                                iph.update("nombreconnexion", Integer.toString(ndreCo), "codepersonne", personne.getCodepersonne());
                                Stage stageBisCai = new Stage();
                                FXMLLoader chargeurBisCai = new FXMLLoader(getClass().getResource("/fxml/MainCaissier.fxml"));
                                Parent rootBisCai = (Parent) chargeurBisCai.load();
                                Scene sceneBisCai = new Scene(rootBisCai);
                                MainCaissierController mainCaissierController = chargeurBisCai.getController();
                                mainCaissierController.setPersonne(personne);
                                this.stageBack = stageBisCai;
                                stageBisCai.setScene(sceneBisCai);
                                stageBisCai.initStyle(StageStyle.UNDECORATED);
                                stageBisCai.show();
                                pic.stopMedia();
                                WindowManager.closeWindow(e);
                                WindowManager.closeWindow(pageprecedente);
                                break;
                            case "Livreur":
                                iph.update("nombreconnexion", Integer.toString(ndreCo), "codepersonne", personne.getCodepersonne());
                                Stage stageBis = new Stage();
                                FXMLLoader chargeurBis = new FXMLLoader(getClass().getResource("/fxml/MainLivreur.fxml"));
                                Parent rootBis = (Parent) chargeurBis.load();
                                Scene sceneBis = new Scene(rootBis);
                                MainLivreurController mainLivreurController = chargeurBis.getController();
                                //mainLivreurController.setPersonne(personne);
                                this.stageBack = stageBis;
                                stageBis.setScene(sceneBis);
                                stageBis.initStyle(StageStyle.UNDECORATED);
                                stageBis.show();
                                pic.stopMedia();
                                WindowManager.closeWindow(e);
                                WindowManager.closeWindow(pageprecedente);
                                break;
                            case "Gérant":
                                iph.update("nombreconnexion", Integer.toString(ndreCo), "codepersonne", personne.getCodepersonne());
                                Stage primaryStage = new Stage();
                                FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fxml/MainGerant.fxml"));
                                Parent root = (Parent) chargeur.load();
                                Scene scene = new Scene(root);
                                MainGerantController mainGerantController = chargeur.getController();
                                mainGerantController.setAdmin(personne);

                                primaryStage.setTitle("Gérant");
                                primaryStage.setScene(scene);
                                primaryStage.show();
                                pic.stopMedia();
                                WindowManager.closeWindow(e);
                                WindowManager.closeWindow(pageprecedente);

                                break;
                            default:
                                break;
                        }
                    }
                } catch (NoResultException err) {
                    reponse.setText("Nom d'utilisateur ou mot de passe incorrect");
                } catch (NullPointerException err) {
                    reponse.setText("Nom d'utilisateur ou mot de passe incorrect");
                }
            }
        } catch (Exception exception) {
        }
    }

    @FXML
    private void btnFermer(MouseEvent e) {
        ((Node) e.getSource()).getScene().getWindow().hide();
    }

}
