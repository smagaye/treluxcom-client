package com.treluxcom.controlleur;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTimePicker;
import com.treluxcom.metier.Boutique;
import com.treluxcom.metier.Employe;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class CalendrierServiceController implements Initializable {

    Boutique boutique = new Boutique();

    public void setBoutique(Boutique boutique) {
        this.boutique = boutique;
    }

    @FXML
    private ToggleGroup jourtravail;
    @FXML
    private JFXTimePicker heuredebut;
    @FXML
    private JFXTimePicker heurefin;
    @FXML
    private JFXDatePicker datePriseService;
    @FXML
    private JFXComboBox<String> employe;
    ObservableList<String> listEmploye = FXCollections.observableArrayList();
    @FXML
    private JFXComboBox<String> fonction;
    ObservableList<String> listFonction = FXCollections.observableArrayList("Caissier", "Gérant", "Livreur");
    @FXML
    private JFXDatePicker dateFinService;
    @FXML
    private JFXRadioButton tousLesJours;
    @FXML
    private JFXRadioButton joursOuvrable;
    @FXML
    private JFXRadioButton weekend;
    @FXML
    private HBox hboxJo;
    @FXML
    private HBox hboxWeek;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ApplicationContext contexte = new ClassPathXmlApplicationContext("spring.xml");
        fonction.setItems(listFonction);
        employe.setItems(listEmploye);
    }

    @FXML
    private void jourTravail(ActionEvent event) {
        if (tousLesJours.isSelected()) {
            hboxWeek.setVisible(false);
            hboxJo.setVisible(false);
        }
        if (joursOuvrable.isSelected()) {
            hboxJo.setVisible(true);
            hboxWeek.setVisible(false);
        }
        if (weekend.isSelected()) {
            hboxWeek.setVisible(true);
            hboxJo.setVisible(false);
        }
    }

    @FXML
    private void planifier(ActionEvent event) {
       datePriseService.getValue();
       System.out.println(datePriseService.getValue().toString());
       if(heuredebut.getValue().toSecondOfDay()>=heurefin.getValue().toSecondOfDay()){
           System.out.println("Impossible");
       }else { System.out.println("Bon");}
 /*          Employe employe= new Employe(boutique,perscais,"caissier");
        new CaissierHome().persist(new Caissier(employe));
    CalendrierserviceId calserid= new CalendrierserviceId();
    calserid.setCodepersonne(perscais.getCodepersonne());
    calserid.setCodejour(2);
    calserid.setDatepriseservice(new Date()); 
    calserid.setHeuredebut(heuredebut.getValue());
    calserid.setHeurefin(heurefin);
    Calendrierservice calser=new Calendrierservice(calserid,employe,new Jour(1));
    new CalendrierserviceHome().persist(calser);
       */
    }

    @FXML
    private void employeEvent(ActionEvent event) {

    }

    @FXML
    private void comboMouseEntered(MouseEvent event) {
    }

    @FXML
    private void fonctionEvent(ActionEvent event) {
        Iterator emps = boutique.getEmployes().iterator();
        listEmploye.removeAll(listEmploye);
        switch (fonction.getValue()) {
            case "Gérant":
                while (emps.hasNext()) {
                    Employe employes = (Employe) emps.next();
                    if (employes.getFonction().equals("Gérant")) {
                        listEmploye.add(employes.getPersonne().getPrenom() + " " + employes.getPersonne().getNom());
                    }
                }

                break;

            case "Caissier":
                while (emps.hasNext()) {
                    Employe employes = (Employe) emps.next();
                    if (employes.getFonction().equals("Caissier")) {
                        listEmploye.add(employes.getPersonne().getPrenom() + " " + employes.getPersonne().getNom());
                    }
                }

                break;
            case "Livreur":
                while (emps.hasNext()) {
                    Employe employes = (Employe) emps.next();
                    if (employes.getFonction().equals("Livreur")) {
                        listEmploye.add(employes.getPersonne().getPrenom() + " " + employes.getPersonne().getNom());
                    }
                }
               
                break;
            default: ;
        }
    }

}
